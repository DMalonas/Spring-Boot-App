package com.weather;


import com.weather.services.client.openweather.OpenWeatherClient;
import com.weather.services.client.openweather.WeatherData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
class OpenWeatherClientTest {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    @Test
    void testGetCurrentWeatherSuccess() {
        double lat = 37.7749;
        double lon = -122.4194;
        Mono<WeatherData> currentWeather = openWeatherClient.getCurrentWeather(lat, lon);
        WeatherData weatherData = currentWeather.block();

        assertNotNull(weatherData);
        assertNotNull(weatherData.main());
        assertNotNull(weatherData.weather().get(0).description());
        assertNotNull(weatherData.main().temp());
    }

    @Test
    void testGetCurrentWeatherFailure() {
        double lat = -200.0;
        double lon = 300.0;
        Mono<WeatherData> currentWeather = openWeatherClient.getCurrentWeather(lat, lon);
        WeatherData weatherData = currentWeather.block();

        assertNull(weatherData);
    }

}

