package com.margarine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class Location {

    @Id
    private long longitude;
    private long latitude;
    private int zip;
    private String country;
    private String state;
    private String city;
    private String street;
}
