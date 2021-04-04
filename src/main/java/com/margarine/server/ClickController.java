package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.margarine.db.ClickItem;
import com.margarine.db.LocationItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
public class ClickController {

    /**
     * THIS CONTROLLER IS USED TO RECORD SHORT_URL CLICKS. IT RECORDS THE CLICK EVENT IN THE DATABASE AND RETURNS AN
     * HTTP.ACCEPTED STATUS TO THE FRONT END IF THE FUNCTION SUCCEEDED, OTHERWISE IT RETURNS AN HTTP ERROR.
     */


    private static class ClickDTO {

        @JsonProperty(value = "shortUrl", required = false)
         private String shortUrl;

        
        @JsonProperty(value = "longitude", required = true)
        private long longitude;

        
        @JsonProperty(value = "latitude", required = true)
        private long latitude;
        
        @JsonProperty(value = "timeClicked", required = true)
        private Date timeClicked;

        public long getLongitude() {
            return longitude;
        }

        public long getLatitude() {
            return latitude;
        }

        public Date getTimeClicked() {
            return timeClicked;
        }

        public String getShortUrl() {
            return shortUrl;
        }

    }
    
    
    @Autowired
    private UrlRepository urlRepository;


    @RequestMapping(
            value = "/{shortUrl}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object clickShortUrl(@PathVariable("shortUrl") String shortUrl, @RequestBody ClickDTO request) {
        
        //Finds a document in the database that matches the short url passed to the function. 
        Optional<UrlItem> match = urlRepository.findById(shortUrl);
        
        //The document was not found in the database
        if(!match.isPresent()){
            return HttpStatus.NOT_FOUND;
        }
        else{
            //Click Item is generated with the necessary information(location, time clicked)
            ClickItem clickItem = new ClickItem(request.getLongitude(), request.getLatitude(), request.getTimeClicked());
            //Url item fetched from the database for update purposes
            UrlItem urlItem = match.get();
            // Extract the original Url to redirect the user
            String originalUrl = urlItem.getOriginalUrl();
            System.out.println("originalUrl = " + originalUrl);
            //Url item gets updated (click item is added to clicks array)
            urlItem.add(clickItem);
            //Database is saved after making the necessary update.
            urlRepository.save(urlItem);
            
            return "redirect:/" + originalUrl;
        }
        
    }

}
