package com.margarine.db;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

//@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
//@AllArgsConstructor  // Lombok helper ^
//@NoArgsConstructor  // Lombok helper ^
public class ClickItem {

    @Id private String id;
    private Date timeClicked;
    private String longitude;
    private String latitude;

    public ClickItem (String longitude, String latitude, Date timeClicked) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeClicked = timeClicked;
    }

    public String getLongitude () {
        return longitude;
    }

    public String getLatitude () {
        return latitude;
    }

    public Date getTimeClicked () {
        return timeClicked;
    }
}
