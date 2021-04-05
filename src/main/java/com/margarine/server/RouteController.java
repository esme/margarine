package com.margarine.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RouteController {

    /** Wildcard route that redirects all root directory GET requests to index.html. Reason is app is an SPA. */
    @RequestMapping(value = "/{path:[^\\.]*}", method = RequestMethod.GET)
    public String redirect() {
        return "forward:/index.html";
    }
}
