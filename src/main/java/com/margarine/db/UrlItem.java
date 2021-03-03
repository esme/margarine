package com.margarine.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class UrlItem {

    @Id
    private long id;                  // random unique object identifier
    private String originalUrl;       // e.g. google.com
    private String shortUrl;          // e.g. margarine.com/goo123
    private int numberOfClicks;       // shortUrl total click counter
    private ArrayList<ClickItem> clicks;  // array of all clicks that match to shortUrl

    public UrlItem(long id, String originalUrl, String shortUrl){
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        numberOfClicks = 0;
        clicks = new ArrayList<>();
    }

    public void add(ClickItem clickItem) {
        clicks.add(clickItem);
        numberOfClicks++;
    }
}
