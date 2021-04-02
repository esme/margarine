package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.margarine.db.ClickItem;
import com.margarine.db.LocationItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.security.ConcurrentMessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.apache.tomcat.util.security.MD5Encoder;


@RestController
public class GenerateController {

    private static class UrlDTO {

        @JsonProperty(value = "originalUrl", required = true)
        private String originalUrl; // google.com

        @JsonProperty(value = "longitude", required = true)
        private long longitude;

        @JsonProperty(value = "latitude", required = true)
        private long latitude;

        @JsonProperty(value = "timeClicked", required = true)
        private Date timeClicked; // dd-MM-YYYY

        @JsonProperty(value = "company", required = false)
        private String company;

        @JsonProperty(value = "shortUrl", required = false)
        private String shortUrl;


        public String getOriginalUrl() {
            return originalUrl;
        }

        public long getLongitude() {
            return longitude;
        }

        public long getLatitude() {
            return latitude;
        }

        public Date getTimeClicked() {
            return timeClicked;
        }

        public String getCompany() {
            return company;
        }

        public String getShortUrl() {
            return shortUrl;
        }
    }

    private static final Logger LOGGER=LoggerFactory.getLogger(GenerateController.class);

    private static final String DOMAIN_NAME = "margarine.com";

    @Autowired private UrlRepository urlRepository;


    @RequestMapping(
            value = "/generate", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody Object generateUrl (@RequestBody UrlDTO request) {

        /*
         * 1. Check if originalUrl already exists in DB
         * 2. If it does not already exist, create a LocationItem A, then a ClickItem B(A), then a UrlItem C(B)
         * 3. Store the UrlItem in the database
         */

        String shortUrl = request.getShortUrl();

        // only try to generate a shortUrl if not specified in the DTO/request
        if (request.getShortUrl() == null) {
            shortUrl = generateShortUrl(request.getOriginalUrl());  // Try to generate a short URL
            LOGGER.info("GENERATED SHORT_URL: " + shortUrl);
        }

        // *STOP* if shortUrl already stored in DB
        if (getShortUrl(shortUrl) != HttpStatus.NOT_FOUND) {
            LOGGER.error("KEY ALREADY EXISTS: PLEASE TRY A DIFFERENT URL.");
            return HttpStatus.ALREADY_REPORTED;
        }

        // record the first click item as the person who generated the click item
        ClickItem clickItem = new ClickItem(request.getLongitude(), request.getLatitude(), request.getTimeClicked());
        LOGGER.info("LATITUDE: " + clickItem.getLatitude()
                + ", LONGITUDE: " + clickItem.getLongitude()
                + ", CLICK_TIME: " + clickItem.getTimeClicked());

        // create the UrlItem to store in DB
        UrlItem urlItem = new UrlItem(request.getOriginalUrl(), shortUrl);
        LOGGER.info("CREATED NEW URL_ITEM { " + urlItem.toString() + " }.");

        // set the company if the parameter was included in the DTO
        if (request.getCompany() != null) {
            urlItem.setCompany(request.getCompany());
            LOGGER.info("SET COMPANY NAME { " + urlItem.toString() + " }.");
        }

        // record the click
        urlItem.add(clickItem);

        try{
            // store the new shortUrl document in the database
            urlRepository.save(urlItem);
            LOGGER.info("URL_ITEM { " + urlItem.toString() + " } WAS SAVED TO THE DATABASE.");

            // craft the JSON payload to return the newly generated shortUrl to the user
            HashMap<String, String> response = new HashMap<>();
            response.put("short_url", shortUrl);
            response.put("original_url", request.getOriginalUrl());
            return response;

        }
        catch(org.springframework.dao.DuplicateKeyException e){
            LOGGER.error("DUPLICATE KEY ERROR: PLEASE TRY DIFFERENT KEY.");
            return HttpStatus.CONFLICT;
        }
    }


    /**
     * Generates short URLs
     * @param originalUrl This is the original URL to which the short URL will be related
     * @return A JSON payload containing the original URL and short URL
     */
    private String generateShortUrl (String originalUrl) {

        if (getShortUrl(originalUrl) == originalUrl){
            return "KEY_ALREADY_EXISTS";
        }

        try{

            //option 1 - MD5
            String urlMd5Hash = MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(originalUrl.getBytes()));
            //String shortUrl = DOMAIN_NAME + "/" + urlMd5Hash.substring(0, 6);
            String shortUrl = urlMd5Hash.substring(0, 6);
            LOGGER.info("GENERATED 6 CHARACTER MD5 HASH FROM URL '" + originalUrl + "'.");

            // option2 - SHA1  (if hash collision)
            if (getShortUrl(shortUrl) != HttpStatus.NOT_FOUND) {
                // this is saying "if the shortUrl we just created already exists in the DB, and its not just a
                // duplicate, then we have a hash collision", i.e. two originalUrl values hashed to the same value
                String urlSha1Hash = MD5Encoder.encode(ConcurrentMessageDigest.digestSHA1(originalUrl.getBytes()));
                shortUrl = urlSha1Hash.substring(0, 6);
            }

            return shortUrl;
        }
        catch (Exception err) {
            LOGGER.error(err.getMessage());
            return "";
        }
    }


    /**
     * Basic method for testing the server's ability to intake a payload as specified by the RequestBody annotation
     * @param request Defines the payload that the server should echo
     * @return Returns ACCEPTED if the test succeeded. Otherwise throws nonsense (heh).
     */
    @RequestMapping(
            value = "/test/print", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus testPrint(@RequestBody final UrlDTO request) {

        try{
            System.out.printf("ORIG_URL=%S\nLONGITUDE=%S\nLATITUDE=%S\nTIME_CLICKED=%S",
                    request.getOriginalUrl(),
                    request.getLongitude(),
                    request.getLatitude(),
                    request.getTimeClicked());

            return HttpStatus.ACCEPTED;
        }
        catch (NullPointerException err) {

            return HttpStatus.I_AM_A_TEAPOT;
        }
    }

    /**
     * Basic query method to fetch UrlItems from the database by index.
     * e.g. Using a web browser: http://localhost:8080/get?short_url=af7335
     * e.g. Using the HTTPie tool: http GET :8080 /get?short_url=af7335
     * @param short_url Specifies the short_url that should be searched for
     * @return Returns the UrlItem wrapped in JSON if it exists. Otherwise returns an HTTP error code.
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody Object findByShortLink(@RequestParam String short_url) {

        Optional<UrlItem> match = urlRepository.findById(short_url);
        if (match.isPresent()) {
            return match.get(); // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


    /**
     * Basic query method to fetch UrlItems from the database by index.
     * e.g. Using a web browser: http://localhost:8080/af7335 --> returns {"originalUrl":"test0.com","shortUrl":"af7335-
     *                                                                     ","numberOfClicks":1,"clicks":[null]}
     * @param shortUrl This is a wildcard path variable that should correlate to the shortUrl the user is trying to fet-
     *                 ch information on.
     * @return Returns the UrlItem wrapped in JSON if it exists. Otherwise returns an HTTP error code.
     */
    @RequestMapping(value = "/get/{shortUrl}", method = RequestMethod.GET)
    public @ResponseBody Object getShortUrl(@PathVariable("shortUrl") String shortUrl) {
        LOGGER.info("Received request: /{shortUrl}");
        LOGGER.info("getShortUrl > PathVariable 'shortUrl' = " + shortUrl);
        Optional<UrlItem> match = urlRepository.findById(shortUrl);
        if (match.isPresent()) {
            return match.get(); // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


}
