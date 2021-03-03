package com.margarine.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class LocationItem {

    @Id
    private long id;
    private long longitude;
    private long latitude;
    //private String zip;  // must be String because integer types will truncate leading zeros
    //private String country;
    //private String state;
    //private String city;
    //private String street;


    public void setId(long id) {
        this.id = id;
    }
}
