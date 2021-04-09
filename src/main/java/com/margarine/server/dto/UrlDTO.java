package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;


/**
 * specifies http request body that user most provide to invoke generate function
 */
@Data
public class UrlDTO {

    @JsonProperty(value = "originalUrl", required = true)
    private String originalUrl; // google.com

    @JsonProperty(value = "longitude", required = false)
    private String longitude;

    @JsonProperty(value = "latitude", required = false)
    private String latitude;

    @JsonProperty(value = "timeClicked", required = false)
    private Date timeClicked; // dd-MM-YYYY

    @JsonProperty(value = "company", required = false)
    private String company;

    @JsonProperty(value = "shortUrl", required = false)
    private String shortUrl;
}
