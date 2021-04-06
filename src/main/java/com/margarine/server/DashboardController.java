package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    private static File apiFile = new File("key.json");


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

        @JsonProperty(value = "shortUrl", required = true)
        private String shortUrl;

        @JsonProperty(value = "originalUrl", required = true)
        private String originalUrl;

        @JsonProperty(value = "company", required = true)
        private String company;

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

            /* *********************************************
             * calculate "Date Last Accessed"
             * ********************************************/
            returnDTO.dateLastAccessed = match.get().getMostRecentClick().toString();


            /* *********************************************
             * calculate "Most Visitors From"
             * ********************************************/
            try{
                returnDTO.mostCommonState = findMostCommonState(match.get().getClicks());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* *********************************************
             * calculate "Visitors Today"
             * ********************************************/
            // counter
            int numberOfVisitorsToday = 0;

            // get the current time
            LocalDateTime preParsedTimeNow = LocalDateTime.now();

            // format the current time - 2099-03-04T00:00:00.000+00:00
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            //dtf.format(preParsedTimeNow);

            // finally, convert the current time to a Date object
            Date now = java.sql.Timestamp.valueOf(preParsedTimeNow);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.format(now);

            LOGGER.info("CurrentTime: " + now);

            // compare the current time to every ClickItem timestamp
            for (ClickItem clickTime: match.get().getClicks()) {
                Date timeClicked = clickTime.getTimeClicked();

                LOGGER.info("ClickItem [" + clickTime.getId() + "] timeClicked = " + timeClicked);

                // compute the difference between now and timeClicked
                long diffInMillies = Math.abs(now.getTime() - timeClicked.getTime());

                LOGGER.info("now.getTime() = " + now.getTime());
                LOGGER.info("timeClicked.getTime() = " + timeClicked.getTime());
                LOGGER.info("diffInMillies = " + diffInMillies);

                long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                LOGGER.info("diffInHours > " + diffInHours);

                // only increment the counter if timeDiff is less than or equal to 24 hours
                if (diffInHours <= 24) {
                    numberOfVisitorsToday++;
                }
            }
            // done!
            returnDTO.numberOfVisitorsToday = String.valueOf(numberOfVisitorsToday);

            LOGGER.info("numberOfVisitorsToday = " + String.valueOf(numberOfVisitorsToday));

            /* *********************************************
             * calculate "Number Of Clicks"
             * ********************************************/
            returnDTO.numberOfClicks = String.valueOf(match.get().getNumberOfClicks());

            /* *********************************************
             * capture default values
             * ********************************************/
            returnDTO.company = match.get().getCompany();
            returnDTO.originalUrl = match.get().getOriginalUrl();
            returnDTO.shortUrl = match.get().getShortUrl();


            // return the data transfer object to the front-end
            return returnDTO; // returns a UrlItem wrapped in JSON if the document exists
        }
        else return HttpStatus.NOT_FOUND; // otherwise returns NOT_FOUND
    }


    /**
     * Converts human addresses to long/lat coordinates
     */
    @RequestMapping(path = "/geocode/{address}", method = RequestMethod.GET)
    public GeocodeResult geocode(@PathVariable("address") String encodedAddress) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String apiKey = readJson(apiFile).get();
        //LOGGER.info("API KEY = " + apiKey);

        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
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

        String apiKey = readJson(apiFile).get();
        //LOGGER.info("API KEY = " + apiKey);

        Request r = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?latlng=" + latitude + "%2C" + longitude + "&language=en")
                .get()
                .addHeader("x-rapidapi-key", apiKey)
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

    private ApiKey readJson(final File file) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory()); // jackson databind
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY);
        return mapper.readValue(file, ApiKey.class);
    }



}
