package com.margarine;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bread")
public class TrackerController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String get(){
        //mapped to hostname:port/bread/
        return "Hello from /bread/";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    String index(){
        //mapped to hostname:port/bread/index/
        return "Hello from /bread/index";
    }

}
