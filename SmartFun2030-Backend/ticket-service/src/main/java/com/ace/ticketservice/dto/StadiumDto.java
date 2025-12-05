package com.ace.ticketservice.dto;


import lombok.Data;
import java.util.List;

@Data
public class StadiumDto {

    private String id;

    private String name;
    private String city;
    private String country;
    private int capacity;

    private double latitude;
    private double longitude;

    private String description;

    private List<String> images;
}

