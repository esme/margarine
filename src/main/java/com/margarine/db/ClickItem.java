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
public class ClickItem {

    @Id
    private long id;            // random unique object identifier
    private String url;         // some user-clicked url that mapped to margarine web app wildcard URI tracker
    private LocationItem location;  // location of user who clicked url
}
