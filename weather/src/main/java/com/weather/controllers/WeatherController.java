package com.weather.controllers;

import com.weather.services.dto.WeatherDto;
import com.weather.services.weather.WeatherService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {


    private WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping
    public ResponseEntity<WeatherDto> getWeather(@RequestParam("ip") String ipAddress) {
        return weatherService.getWeather(ipAddress)
                .map(weatherInfo -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(weatherInfo))
                .orElse(ResponseEntity.notFound().build());
    }
}
