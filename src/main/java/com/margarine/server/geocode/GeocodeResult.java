package com.margarine.server.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.margarine.server.geocode.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeResult {

    List<GeocodeObject> results;
    String status;

    public GeocodeResult() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodeObject> getResults() {
        return results;
    }

    public void setResults(List<GeocodeObject> results) {
        this.results = results;
    }
}
