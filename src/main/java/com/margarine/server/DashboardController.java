package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.margarine.db.ClickItem;
import com.margarine.db.UrlItem;
import com.margarine.db.UrlRepository;
import com.margarine.server.geocode.AddressComponent;
import com.margarine.server.geocode.GeocodeObject;
import com.margarine.server.geocode.GeocodeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

@RestController
public class DashboardController {

    // read or write from database using this object
    @Autowired
    private UrlRepository urlRepository;

    // log to spring boot console using this object
    private static final Logger LOGGER= LoggerFactory.getLogger(GenerateController.class);

    /**
     * Data transfer object class - specifies the required payload to use the reverseGeocode() function
     */
    private static class CoordinatesDTO {

        @JsonProperty(value = "longitude", required = true)
        private String longitude;

        @JsonProperty(value = "latitude", required = true)
        private String latitude;

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

    private static class DashboardMetricsDTO {

        @JsonProperty(value = "numberOfClicks", required = true)
        private String numberOfClicks;

        @JsonProperty(value = "mostCommonState", required = true)
        private String mostCommonState;

        @JsonProperty(value = "dateLastAccessed", required = true)
        private String dateLastAccessed;

        @JsonProperty(value = "numberOfVisitorsToday", required = true)
        private String numberOfVisitorsToday;

    }

    /**
     * Basic query method to fetch UrlItems from the database by index.
     * e.g. Using a web browser: http://localhost:8080/get?short_url=af7335
     * e.g. Using the HTTPie tool: http GET :8080 /get?short_url=af7335
     * @param short_url Specifies the short_url that should be searched for
     * @return Returns the UrlItem wrapped in JSON if it exists. Otherwise returns an HTTP error code.
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @org.springframework.web.bind.annotation.ResponseBody Object findByShortLink(@RequestParam String short_url) {

        Optional<UrlItem> match = urlRepository.findById(short_url);
        if (match.isPresent()) {
            return match.get(); // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


    /**
     * Basic query method to fetch UrlItems from the database by index.
     * e.g. Using a web browser: http://localhost:8080/get/af7335 --> returns {"originalUrl":"test0.com","shortUrl":"af7335-
     *                                                                     ","numberOfClicks":1,"clicks":[null]}
     * @param shortUrl This is a wildcard path variable that should correlate to the shortUrl the user is trying to fet-
     *                 ch information on.
     * @return Returns the UrlItem wrapped in JSON if it exists. Otherwise returns an HTTP error code.
     */
    @RequestMapping(value = "/get/{shortUrl}", method = RequestMethod.GET)
    public @org.springframework.web.bind.annotation.ResponseBody Object getShortUrl(@PathVariable("shortUrl") String shortUrl) {
        LOGGER.info("Received request: /{shortUrl}");
        LOGGER.info("getShortUrl > PathVariable 'shortUrl' = " + shortUrl);

        // locate the specified shortUrl in the database
        Optional<UrlItem> match = urlRepository.findById(shortUrl);

        // create placeholder for return object
        DashboardMetricsDTO returnDTO = new DashboardMetricsDTO();

        // perform dashboard metric computation if there was a match
        if (match.isPresent()) {

            // TODO calculate "Date Last Accessed"
            // (get curTime; iterate over clicks; match item.timeClicked with the smallest delta)


            // calculate "Most Visitors From"
            try{
                returnDTO.mostCommonState = findMostCommonState(match.get().getClicks());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO calculate "Visitors Today"

            return match.get(); // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


    /**
     * Converts human addresses to long/lat coordinates
     */
    @RequestMapping(path = "/geocode/{address}", method = RequestMethod.GET)
    public GeocodeResult geocode(@PathVariable("address") String encodedAddress) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-key", "129514bae2mshb9121150050ff42p1a3075jsn7ca455f54fca")
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .build();

        ResponseBody responseBody = client.newCall(request).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody.string(), GeocodeResult.class);
    }


    /**
     * Converts long/lat coordinates to human-readable addresses
     */
    @RequestMapping(path = "/geocode/reverse", method = RequestMethod.GET)
    public GeocodeResult reverseGeocode (@RequestBody CoordinatesDTO request) throws IOException {

        String longitude = request.getLongitude();
        String latitude = request.getLatitude();

        return computeReverseGeocode(longitude, latitude);
    }



    /**
     * Converts long/lat coordinates to human-readable addresses
     */
    @RequestMapping(path = "/geocode/reverse/{coordinates}", method = RequestMethod.GET)
    public GeocodeResult reverseGeocode (@PathVariable("coordinates") String request) throws IOException {

        String longitude = request.split(",")[0];
        String latitude = request.split(",")[1];

        return computeReverseGeocode(longitude, latitude);
    }


    private GeocodeResult computeReverseGeocode (String latitude, String longitude) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request r = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?latlng=" + latitude + "%2C" + longitude + "&language=en")
                .get()
                .addHeader("x-rapidapi-key", "129514bae2mshb9121150050ff42p1a3075jsn7ca455f54fca")
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .build();

        ResponseBody responseBody = client.newCall(r).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(responseBody.string(), GeocodeResult.class);

    }


    /** Analyze the clicks ArrayList to determine the most common location by state */
    private String findMostCommonState (ArrayList<ClickItem> clicks) throws IOException {
        String longitude;
        String latitude;
        ArrayList<String> states = new ArrayList<>();

        // iterate over the ClickItems array and analyze it to determine the most common location by state
        for (ClickItem elem: clicks) {
            longitude = String.valueOf(elem.getLongitude());
            latitude = String.valueOf(elem.getLatitude());

            // reverse geocode the coordinates which are stored in the ClickItem
            GeocodeResult location = computeReverseGeocode(latitude, longitude);

            // we have to dig deep into the geolocation results to find the state...
            // results.0.address_components.{item where types includes 'administrative_area_level_1'}.short_name
            outer:
            for (GeocodeObject g: location.getResults()) {
                for (AddressComponent a: g.getAddressComponents()) {
                    LOGGER.info("findMostCommonState > AddressComponent > " + a.toString());
                    if (a.getTypes().contains("administrative_area_level_1")){
                        states.add(a.getShortName());
                        break outer;
                        // only need to grab the first occurrence of a state because google will return several approx.
                        //locations. This is because long/lat reverse geo-coding only *approximates* to exact addresses.
                    }
                }
            }
            // keep going until all of the clicks have been analyzed (reverse geo-coded and parsed for 2-char
            // state code (e.g., MA, NH, NY, etc.)
        }
        // all clicks have been analyzed!

        // uncomment for testing...
        //states = new ArrayList<>(Arrays. asList("MA", "MA", "MA", "NH", "NH", "RI"));

        LOGGER.info("findMostCommonState > states = : " + states.toString());

        // now analyze the list to find the state that occurs most frequently

        // track the highest count and corresponding state
        int curHighestCount = 0;
        String curMostFrequentState = "";

        // iterate over the states
        for (String s: states) {

            // for every element, count the number of times it occurs in the list
            int count = Collections.frequency(states, s);

            // if the count is higher than the current highest, then set the new high
            if (count > curHighestCount) {
                curHighestCount = count;
                curMostFrequentState = s;
            }
        }
        LOGGER.info("findMostCommonState > " + curMostFrequentState);
        return curMostFrequentState;
    }



}