package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class ClickDTO {

    @JsonProperty(value = "shortUrl", required = false)
    private String shortUrl;


    @JsonProperty(value = "longitude", required = true)
    private Float longitude;


    @JsonProperty(value = "latitude", required = true)
    private Float latitude;

    @JsonProperty(value = "timeClicked", required = true)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date timeClicked;

    public String toString() {

        return (shortUrl == null) ? "longitude=" + longitude + ", latitude=" + latitude + ", timeClicked=" + timeClicked
                : "shortUrl: " + shortUrl + ", longitude: " + longitude + ", latitude: " + latitude + ", timeClicked: " + timeClicked;
    }
}
