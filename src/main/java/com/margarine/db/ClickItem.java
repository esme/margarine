package com.margarine.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

//@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
//@AllArgsConstructor  // Lombok helper ^
//@NoArgsConstructor  // Lombok helper ^
public class ClickItem {

    @Id private String id;
    private Date timeClicked;
    private long longitude;
    private long latitude;

    public ClickItem (long longitude, long latitude, Date timeClicked) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeClicked = timeClicked;
    }

    public long getLongitude () {
        return longitude;
    }

    public long getLatitude () {
        return latitude;
    }

    public String getTimeClicked () {
        return timeClicked.toString();
    }
}
