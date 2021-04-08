package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * This is a nested/inner data component of the CoordinateDTO
 */
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
public class Coordinate {

    @JsonProperty("state")
    private String state;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("latitude")
    private String latitude;

    public Coordinate(String latitude, String longitude, String state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }
}
