package com.margarine.server;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class FrontEndRouteController {


    // TODO IMPLEMENT ANY DATA TRANSFER OBJECTS (DTOs) USED BY THIS CONTROLLER AS PRIVATE CLASSES AT THE TOP



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody HttpStatus index () {
        /**
         * TODO IMPLEMENT THIS METHOD. IT RETURNS THE DEFAULT/ROOT HTML FILE WHEN CLIENT REQUESTS '/' URI
         */
        return HttpStatus.ACCEPTED;
    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public @ResponseBody HttpStatus home () {
        /**
         * TODO IMPLEMENT THIS METHOD. IT RETURNS THE HOME HTML FILE WHEN CLIENT REQUESTS '/HOME' URI
         */
        return HttpStatus.ACCEPTED;
    }


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public @ResponseBody HttpStatus dashboard () {
        /**
         * TODO IMPLEMENT THIS METHOD. IT RETURNS THE DASHBOARD HTML FILE WHEN CLIENT REQUESTS '/DASHBOARD' URI
         */
        return HttpStatus.ACCEPTED;
    }




}
