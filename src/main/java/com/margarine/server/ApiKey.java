package com.margarine.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiKey {

    @JsonProperty
    private String rapid_api_key;

    private ApiKey() {
    }

    public ApiKey(String rapid_api_key) {
        this();
        this.rapid_api_key = rapid_api_key;
    }

    public String get() {
        return rapid_api_key;
    }
}