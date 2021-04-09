package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

/**
 * This is a nested/inner data component of the CoordinateDTO
 */
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
public class Coordinate {

    @JsonProperty("state")
    private String state;

    @JsonProperty("longitude")
    private Float longitude;

    @JsonProperty("latitude")
    private Float latitude;

    @JsonProperty("timeClicked")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date timeClicked;

    public Coordinate(Float latitude, Float longitude, String state, Date timeClicked) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.timeClicked = timeClicked;
    }
}
