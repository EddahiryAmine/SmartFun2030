package com.ace.matchstadiumservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document("stadiums")
public class Stadium {

    @Id
    private String id;

    private String name;
    private String city;
    private String country;
    private int capacity;

    private double latitude;
    private double longitude;

    private String description;

    // Plusieurs images du stade
    private List<String> images;

    // URL automatique vers Google Maps (optionnel)
    public String getGoogleMapsUrl() {
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }
}
