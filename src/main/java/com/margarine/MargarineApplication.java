package com.margarine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class MargarineApplication{

    public static void main(String[] args) {
        SpringApplication.run(MargarineApplication.class, args);
    }

    //@Autowired
    //private UrlRepository urlRepository;

    //@Override
    //public void run(String... args) throws Exception {

        // Save the employees
        //urlRepository.save(new UrlItem(1, "google.com", "goo123"));

        // Fetch the all URLs
        //List<UrlItem> allUrlsInDb = urlRepository.findAll();
        //System.out.println("============ List of all Stored URLs ============");
        //System.out.println(allUrlsInDb);
    //}

}
