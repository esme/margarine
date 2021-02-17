package com.margarine;

public class Url {

    private final long id;
    private final String originalUrl;
    private String shortUrl;

    public Url (long id, String originalUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.setShortUrl(originalUrl);
    }

    public long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String originalUrl) {
        // TODO implement shortURl generator code here
    }
}
