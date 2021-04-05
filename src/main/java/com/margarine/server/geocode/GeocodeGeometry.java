package com.margarine.server.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.margarine.server.geocode.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeGeometry {

    @JsonProperty("location")
    GeocodeLocation geocodeLocation;

    public GeocodeGeometry() {
    }

    public GeocodeLocation getGeocodeLocation() {
        return geocodeLocation;
    }

    public void setGeocodeLocation(GeocodeLocation geocodeLocation) {
        this.geocodeLocation = geocodeLocation;
    }
}