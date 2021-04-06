package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.HashMap;
import org.apache.tomcat.util.security.MD5Encoder;


@RestController
public class GenerateController {

    // specifies http request body that user most provide to invoke generate function
    private static class UrlDTO {

        @JsonProperty(value = "originalUrl", required = true)
        private String originalUrl; // google.com

        @JsonProperty(value = "longitude", required = false)
        private String longitude;

        @JsonProperty(value = "latitude", required = false)
        private String latitude;

        @JsonProperty(value = "timeClicked", required = false)
        private Date timeClicked; // dd-MM-YYYY

        @JsonProperty(value = "company", required = false)
        private String company;

        @JsonProperty(value = "shortUrl", required = false)
        private String shortUrl;


        public String getOriginalUrl() {
            return originalUrl;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
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

    // log to spring boot console using this object
    private static final Logger LOGGER=LoggerFactory.getLogger(GenerateController.class);

    // read or write from database using this object
    @Autowired private UrlRepository urlRepository;

    @Autowired private DashboardController dashboardController;


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

        System.out.print("Request :");
        System.out.print(request);

        String shortUrl = request.getShortUrl();

        // only try to generate a shortUrl if not specified in the DTO/request
        if (request.getShortUrl() == null || request.getShortUrl().length() == 0) {
            shortUrl = generateShortUrl(request.getOriginalUrl());  // Try to generate a short URL
            LOGGER.info("GENERATED SHORT_URL: " + shortUrl);
        }

        // *STOP* if shortUrl already stored in DB
        if (dashboardController.getShortUrl(shortUrl) != HttpStatus.NOT_FOUND) {
            LOGGER.error("KEY ALREADY EXISTS: PLEASE TRY A DIFFERENT URL.");
            return HttpStatus.ALREADY_REPORTED;
        }

        // record the first click item as the person who generated the click item
        // ClickItem clickItem = new ClickItem(request.getLongitude(), request.getLatitude(), request.getTimeClicked());
        // LOGGER.info("LATITUDE: " + clickItem.getLatitude()
        //         + ", LONGITUDE: " + clickItem.getLongitude()
        //         + ", CLICK_TIME: " + clickItem.getTimeClicked());

        // create the UrlItem to store in DB
        UrlItem urlItem = new UrlItem(request.getOriginalUrl(), shortUrl);
        LOGGER.info("CREATED NEW URL_ITEM { " + urlItem.toString() + " }.");

        // set the company if the parameter was included in the DTO
        if (request.getCompany() != null) {
            urlItem.setCompany(request.getCompany());
            LOGGER.info("SET COMPANY NAME { " + urlItem.toString() + " }.");
        }

        // record the click
        // urlItem.add(clickItem);

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

        if (dashboardController.getShortUrl(originalUrl) == originalUrl){
            return "KEY_ALREADY_EXISTS";
        }

        try{
            //option 1 - MD5
            String urlMd5Hash = MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(originalUrl.getBytes()));
            String shortUrl = urlMd5Hash.substring(0, 6);
            LOGGER.info("GENERATED 6 CHARACTER MD5 HASH FROM URL '" + originalUrl + "'.");

            // option2 - SHA1  (if hash collision)
            if (dashboardController.getShortUrl(shortUrl) != HttpStatus.NOT_FOUND) {
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
}
