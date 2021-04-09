package com.margarine.db;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
public class ClickItem {

    @Id private String id;
    private Date timeClicked;
    private Float longitude;
    private Float latitude;

    public ClickItem (Float longitude, Float latitude, Date timeClicked) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeClicked = timeClicked;
    }

    public Float getLongitude () {
        return longitude;
    }

    public Float getLatitude () {
        return latitude;
    }

    public Date getTimeClicked () {
        return timeClicked;
    }
}
