package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.margarine.db.ClickItem;
import com.margarine.db.LocationItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import org.apache.tomcat.util.security.ConcurrentMessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Optional;

import org.apache.tomcat.util.security.MD5Encoder;


@RestController
public class GenerateController {

    private static class UrlDTO {

        @JsonProperty("originalUrl")
        private String originalUrl; // google.com

        @JsonProperty("longitude")
        private long longitude;

        @JsonProperty("latitude")
        private long latitude;

        @JsonProperty("timeClicked")
        private Date timeClicked; // dd-MM-YYYY


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
    }

    private static final Logger LOGGER=LoggerFactory.getLogger(GenerateController.class);

    private static long id = 0;

    @Autowired private UrlRepository urlRepository;


    @RequestMapping(
            value = "/generate", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus generateUrl (@RequestBody UrlDTO request) {

        /*
         * 1. Check if originalUrl already exists in DB
         * 2. If it does not already exist, create a LocationItem A, then a ClickItem B(A), then a UrlItem C(B)
         * 3. Store the UrlItem in the database
         */
        String uniqueId = String.valueOf(id++); // set ID then increment
        LOGGER.info("SET UNIQUE_ID: " + uniqueId);

        String shortUrl = generateShortUrl(request.getOriginalUrl());  // Try to generate a short URL
        LOGGER.info("GENERATED SHORT_URL: " + shortUrl);

        LocationItem locationItem = new LocationItem(uniqueId, request.getLongitude(), request.getLatitude()); // Capture where the click came from
        LOGGER.info("SET LATITUDE: " + locationItem.getLatitude() + ", LONGITUDE: " + locationItem.getLongitude());

        ClickItem clickItem = new ClickItem(uniqueId, locationItem, request.getTimeClicked()); // Capture the click
        LOGGER.info("SET CLICK_TIME: " + clickItem.getTimeClicked());

        UrlItem urlItem = new UrlItem(uniqueId, request.getOriginalUrl(), shortUrl); // Create the UrlItem to store in DB
        LOGGER.info("CREATED NEW URL_ITEM { " + urlItem.toString() + " }.");

        urlItem.add(clickItem); // Save the click that created the UrlItem

        try{
            urlRepository.save(urlItem);
            LOGGER.info("URL_ITEM { " + urlItem.toString() + " } WAS SAVED TO THE DATABASE.");
        }
        catch(org.springframework.dao.DuplicateKeyException e){
            LOGGER.error("DUPLICATE KEY ERROR: PLEASE TRY DIFFERENT KEY.");
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.ACCEPTED;
    }


    /**
     *
     * @param originalUrl
     * @return
     */
    private String generateShortUrl (String originalUrl) {
        try{
            // TODO Check DB for originalUrl
            // TODO Generate and return short URL

            // try fetching the document from Mongo containing the specified originalUrl
            //urlRepository.findById()

            //option1
            //String shortUrl = DigestUtils.md5DigestAsHex(originalUrl.getBytes());

            //option 2
            String urlMd5Hash = MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(originalUrl.getBytes()));
            String shortUrl = urlMd5Hash.substring(0, 6);
            LOGGER.info("GENERATED 6 CHARACTER MD5 HASH FROM URL '" + originalUrl + "'.");
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
    @RequestMapping(
            value = "/get", method = RequestMethod.GET
    )
    public @ResponseBody Object findByShortLink(@RequestParam String short_url) {

        Optional<UrlItem> match = urlRepository.findById(short_url);
        if (match.isPresent()) {
            return match.get(); // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


}
