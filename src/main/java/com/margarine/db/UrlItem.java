package com.margarine.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;


@Document(collection = "ShortUrls")  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class UrlItem {

    //@Id
    //private String id;                  // random unique object identifier

    @Indexed(name = "original_url_index", unique=true)
    private String originalUrl;       // e.g. google.com

    @Id @Indexed(name = "short_url_index")
    private String shortUrl;          // e.g. margarine.com/goo123

    private int numberOfClicks;       // shortUrl total click counter

    @DBRef
    private ArrayList<ClickItem> clicks;  // array of all clicks that match to shortUrl


    public UrlItem(String id, String originalUrl, String shortUrl){
        //this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        numberOfClicks = 0;
        clicks = new ArrayList<>();
    }

    public void add(ClickItem clickItem) {
        clicks.add(clickItem);
        numberOfClicks++;
    }

    @Override
    public String toString() {
        return String.format(
                "UrlItem[id=NULL, " +
                        "originalUrl='%s', " +
                        "shortUrl='%s" +
                        "numberOfClicks=%s" +
                        "clicks=%s" +
                        "']",
                originalUrl, shortUrl, numberOfClicks, clicks.toString());
    }
}
