package com.weather.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    private Instant timeStamp;
    private Double lon;

    private Double lat;

    private String main;

    private String description;

    private Double temp;

    private String country;


    public Weather() {
    }

    public Weather(String ip, Instant timeStamp, Double lon, Double lat, String main, String description, Double temp, String country) {
        this.ip = ip;
        this.timeStamp = timeStamp;
        this.lon = lon;
        this.lat = lat;
        this.main = main;
        this.description = description;
        this.temp = temp;
        this.country = country;
    }
}

