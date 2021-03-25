package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.margarine.db.ClickItem;
import com.margarine.db.LocationItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class ClickController {

    /**
     * THIS CONTROLLER IS USED TO RECORD SHORT_URL CLICKS. IT RECORDS THE CLICK EVENT IN THE DATABASE AND RETURNS AN
     * HTTP.ACCEPTED STATUS TO THE FRONT END IF THE FUNCTION SUCCEEDED, OTHERWISE IT RETURNS AN HTTP ERROR.
     */


    private class ClickDTO {
        // TODO DEFINE A DATA TRANSFER OBJECT THAT SPECIFIES THE REQUIRED API BODY/PAYLOAD THAT THE CLIENT MUST SUPPLY
        @JsonProperty("shortUrl")
         private String shortUrl;
        
        @JsonProperty("latitude")
        private long latitude;
        
        @JsonProperty("longitude")
        private long longitude;
        
        @JsonProperty("ipAddress")
        private String ipAddress;
        
        @JsonProperty("timeClicked") //Payload parameter was added
        private Date timeClicked;

        public String getShortUrl() {
            return shortUrl;
        }

        public long getLatitude() {
            return latitude;
        }

        public long getLongitude() {
            return longitude;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public Date getTimeClicked() {
            return timeClicked;
        }
        
    }
    
    private static final Logger LOGGER=LoggerFactory.getLogger(ClickController.class);
    
    private static long id = 0;

    @Autowired
    private UrlRepository urlRepository;


    @RequestMapping(
            value = "/click", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus clickShortUrl(@RequestBody ClickDTO request) {
        
        String uniqueId = String.valueOf(id++);
        LOGGER.info("SET UNIQUE_ID: " + uniqueId);
        
        LocationItem locationItem = new LocationItem(uniqueId, request.getLongitude(), request.getLatitude());
        LOGGER.info("SET LATITUDE: " + locationItem.getLatitude() + ", LONGITUDE: " + locationItem.getLongitude());
        
        ClickItem clickItem = new ClickItem(uniqueId, locationItem, request.getTimeClicked());
        LOGGER.info("SET CLICK_TIME: " + clickItem.getTimeClicked());
        
        UrlItem urlItem = urlRepository.findUrlItemByShortUrl(request.getShortUrl());
        LOGGER.info("FOUND URL_ITEM { " + urlItem.toString() + " }.");
        
        updateClickList(urlItem, clickItem);

        return HttpStatus.ACCEPTED;
    }
    
    public void updateClickList(UrlItem urlItem, ClickItem clickItem){
        urlItem.add(clickItem);
        urlRepository.save(urlItem);
    }

}
