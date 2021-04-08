package com.margarine.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;


@Document(collection = "ShortUrls")  // marks this class as defining a MongoDB document data model
@Data  // Lombok helper that save us from the drudgery of creating various getters, setters, and constructors.
@AllArgsConstructor  // Lombok helper ^
@NoArgsConstructor  // Lombok helper ^
public class UrlItem {

    @Indexed(name = "original_url_index", unique=true)  private String originalUrl;  // e.g. google.com
    @Id @Indexed(name = "short_url_index") private String shortUrl;  // e.g. margarine.com/goo123
    private int numberOfClicks;  // shortUrl total click counter
    private ArrayList<ClickItem> clicks;  // array of all clicks that match to shortUrl
    private String company;  // optional company name
    private Date mostRecentClick;
    private Date dateCreated;


    /** default constructor */
    public UrlItem(String originalUrl, String shortUrl, Date dateCreated){
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        numberOfClicks = 0;
        clicks = new ArrayList<>();
        this.dateCreated = dateCreated;
    }

    /** mutator method for company */
    public void setCompany (String company) {
        this.company = company;
    }

    /** appends a new click item to the click array */
    public void add(ClickItem clickItem) {
        mostRecentClick = clickItem.getTimeClicked();
        clicks.add(clickItem);
        numberOfClicks++;
    }

    /** pretty prints the UrlItem */
    @Override
    public String toString() {
        if (this.company != null) {
            return String.format(
                    "UrlItem[originalUrl='%s', " +
                            "shortUrl='%s, " +
                            "company='%s', " +
                            "numberOfClicks=%s, " +
                            "clicks=%s" +
                            "']",
                    originalUrl, shortUrl, company, numberOfClicks, clicks.toString());

        }
        return String.format(
                "UrlItem[originalUrl='%s', " +
                        "shortUrl='%s, " +
                        "numberOfClicks=%s, " +
                        "clicks=%s" +
                        "']",
                originalUrl, shortUrl, numberOfClicks, clicks.toString());
    }
}
