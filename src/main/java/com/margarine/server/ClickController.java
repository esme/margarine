package com.margarine.server;


import com.margarine.db.ClickItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import java.util.Optional;
import com.margarine.server.dto.ClickDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
public class ClickController {

    /**
     * THIS CONTROLLER IS USED TO RECORD SHORT_URL CLICKS. IT RECORDS THE CLICK EVENT IN THE DATABASE AND RETURNS AN
     * HTTP.ACCEPTED STATUS TO THE FRONT END IF THE FUNCTION SUCCEEDED, OTHERWISE IT RETURNS AN HTTP ERROR.
     */
    
    
    @Autowired
    private UrlRepository urlRepository;


    /**
     * Treat all routes as /{shortUrl} clicks except: '/', '/index.html, and '/dashboard'
     */
    @RequestMapping(value = "/click/{shortUrl}")
    public String clickShortUrl(@PathVariable("shortUrl") String shortUrl, @RequestBody ClickDTO request) {

        //Finds a document in the database that matches the short url passed to the function. 
        Optional<UrlItem> match = urlRepository.findById(shortUrl);
        
        //The document was not found in the database
        if(!match.isPresent()){
            return "forward:/index.html";
        }
        else{
            //Click Item is generated with the necessary information(location, time clicked)
            ClickItem clickItem = new ClickItem(request.getLongitude(), request.getLatitude(), request.getTimeClicked());

            //Url item fetched from the database for update purposes
            UrlItem urlItem = match.get();

            // Extract the original Url to redirect the user
            String originalUrl = urlItem.getOriginalUrl();

            //Url item gets updated (click item is added to clicks array)
            urlItem.add(clickItem);

            //Database is saved after making the necessary update.
            urlRepository.save(urlItem);
            
            return "redirect:/" + originalUrl;
        }
        
    }

}
