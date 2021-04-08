package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;


/**
 * Data transfer object class for /get/{shortUrl}/coordinates/
 */
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
public class CoordinatesDTO {

    @JsonProperty("coordinates")
    private ArrayList<Coordinate> coordinates = new ArrayList<>();


    public void add(Coordinate coordinate) {
        coordinates.add(coordinate);
    }
}
