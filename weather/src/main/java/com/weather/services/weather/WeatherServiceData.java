package com.weather.services.weather;

import com.weather.domain.Weather;
import com.weather.domain.WeatherRepository;
import com.weather.services.client.openweather.WeatherData;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WeatherServiceData {

    private final WeatherRepository weatherRepository;

    public WeatherServiceData(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Transactional
    public Weather saveWeatherData(String ip, WeatherData weatherData) {
        Weather weather = new Weather(ip,
                Instant.now(),
                weatherData.coord().lon(),
                weatherData.coord().lat(),
                weatherData.weather().get(0).main(),
                weatherData.weather().get(0).description(),
                weatherData.main().temp(),
                weatherData.sys().country()
        );
        Weather savedWeather = weatherRepository.save(weather);
        return savedWeather;
    }

}
