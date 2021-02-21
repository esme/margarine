package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UrlDTO {

    @JsonProperty("originalUrl")
    private String originalUrl;

    @JsonProperty("longitude")
    private long longitude;

    @JsonProperty("latitude")
    private long latitude;

    @JsonProperty("timeClicked")
    private Date timeClicked;


    public String getOriginalUrl() {
        return originalUrl;
    }

    public long getLongitude() {
        return longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public Date getTimeClicked() {
        return timeClicked;
    }
}
