package com.margarine.server;

import com.margarine.db.ClickItem;
import com.margarine.db.LocationItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class SpringController {

    private static long id = 0;

    @Autowired
    private UrlRepository urlRepository;


    @RequestMapping(
            value = "/url", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus generateUrl (@RequestBody UrlDTO request) {

        /*
         * 1. Check if originalUrl already exists in DB
         * 2. If it does not already exist, create a LocationItem A, then a ClickItem B(A), then a UrlItem C(B)
         * 3. Store the UrlItem in the database
         */
        String shortUrl = generateShortUrl(request.getOriginalUrl());  // Try to generate a short URL

        LocationItem locationItem = new LocationItem(id, request.getLongitude(), request.getLatitude()); // Capture where the click came from

        ClickItem clickItem = new ClickItem(id, shortUrl, locationItem, request.getTimeClicked()); // Capture the click

        UrlItem urlItem = new UrlItem(id, request.getOriginalUrl(), shortUrl); // Create the UrlItem to store in DB

        urlItem.add(clickItem); // Save the click that created the UrlItem

        try{
            urlRepository.save(urlItem);
            id++;
            return HttpStatus.ACCEPTED;
        }
        catch(org.springframework.dao.DuplicateKeyException e){
            System.out.print("DUPLICATE KEY FAILURE!");
            return HttpStatus.CONFLICT;
        }
    }


    @RequestMapping(
            value = "/test", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus test(@RequestBody final UrlDTO request) {

        System.out.printf("ORIG_URL=%S\nLONGITUDE=%S\nLATITUDE=%S\nTIME_CLICKED=%S",
                request.getOriginalUrl(),
                request.getLongitude(),
                request.getLatitude(),
                request.getTimeClicked());

        return HttpStatus.ACCEPTED;
    }


    private String generateShortUrl (String originalUrl) {
        try{
            // TODO Check DB for originalUrl
            // TODO Generate and return short URL
            return "" + String.valueOf(id);
        }catch (Exception err) {
            System.out.print("URL already exists.");
        }
        return "";
    }

}
