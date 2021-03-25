package com.margarine.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

@Repository
@RepositoryRestResource(collectionResourceRel = "url", path = "url")
public interface UrlRepository extends MongoRepository<UrlItem, String> {
    /**
     * The MongoRepository superclass includes all the necessary code for reading and writing our domain class to and
     * from the database.
     *
     * The @RepositoryRestResource annotation tells Spring Boot to automatically generate a REST endpoint for the data
     * using the HAL JSON specification.
     */
    @Query(value = "{'shortUrl': ?0}", fields = "{'shortUrl' : 0}")
    public UrlItem findUrlItemByShortUrl(String shortUrl);
    
}
