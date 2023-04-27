package com.weather.services.weather;

import com.weather.services.dto.WeatherDto;

import java.util.Optional;

public interface WeatherService {
    Optional<WeatherDto> getWeather(String ip);
}