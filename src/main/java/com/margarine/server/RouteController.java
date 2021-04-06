package com.margarine.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class RouteController {

    /** Redirect all '/' and '/dashboard/ requests to index.html. */
    @GetMapping(value = {"/", "/dashboard"})
    public String redirect() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "{_:^(?!index\\.html|dashboard|user-tracking.html).*$}")
    public String waitingPage() {
        return "forward:/user-tracking/user-tracking.html";
    }

    //    /** Wildcard route that redirects all root directory GET requests to index.html. Reason is app is an SPA. */
    //    @RequestMapping(value = "/{path:[\\.]*}", method = RequestMethod.GET)
    //    public String redirectShortUrlClick() {
    //        return "forward:/index.html";
    //    }
}
