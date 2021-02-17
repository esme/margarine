package com.margarine;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class GeneratorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String get(){
        //mapped to hostname:port/
        return "Hello from /";
        // TODO redirect to dashboard
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    String generate(@RequestParam("orig_url") String originalUrl) {
        // TODO create Url object and return to client
        //mapped to hostname:port/generate
        return "Hello from /generate";
    }



}
