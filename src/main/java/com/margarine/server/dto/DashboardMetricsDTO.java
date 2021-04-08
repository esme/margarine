package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Data transfer object class - specifies the required payload to use the reverseGeocode() function
 */
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
public class DashboardMetricsDTO {

    @JsonProperty(value = "numberOfClicks", required = true)
    private String numberOfClicks;

    @JsonProperty(value = "mostVisitorsFrom", required = true)
    private String mostVisitorsFrom;

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