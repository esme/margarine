package com.margarine.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
//@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class ClickItem {

    @Id
    private String id;            // random unique object identifier
    private LocationItem location;  // location of user who clicked url
    private Date timeClicked;

    public ClickItem (String id, LocationItem location, Date timeClicked) {
        this.id = id;
        this.location = location;
        this.timeClicked = timeClicked;
    }
}
