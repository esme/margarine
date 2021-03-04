package com.margarine.server;

import com.margarine.db.UrlRepository;
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
    }


    @Autowired
    private UrlRepository urlRepository;


    @RequestMapping(
            value = "/click/**", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody HttpStatus clickShortUrl(@RequestBody ClickDTO request) {

        /*
         * TODO IMPLEMENT THIS
         */
        return HttpStatus.ACCEPTED;
    }

}

