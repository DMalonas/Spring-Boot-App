package com.weather;

import static org.mockito.Mockito.*;

import com.weather.domain.Weather;
import com.weather.services.client.ipapi.IpApiClient;
import com.weather.services.client.ipapi.IpApiResult;
import com.weather.services.client.openweather.OpenWeatherClient;
import com.weather.services.client.openweather.WeatherData;
import com.weather.services.dto.WeatherDto;
import com.weather.services.weather.WeatherServiceData;
import com.weather.services.weather.WeatherServiceImpl;
import reactor.core.publisher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeatherServiceImplTest {

    private IpApiClient ipApiClient;
    private OpenWeatherClient openWeatherClient;
    private WeatherServiceData weatherServiceData;
    private WeatherServiceImpl weatherServiceImpl;

    @BeforeEach
    void setUp() {
        ipApiClient = mock(IpApiClient.class);
        openWeatherClient = mock(OpenWeatherClient.class);
        weatherServiceData = mock(WeatherServiceData.class);
        weatherServiceImpl = new WeatherServiceImpl(ipApiClient, openWeatherClient, weatherServiceData);
    }

    @Test
    void testGetWeather() {
        // Test data
        String ip = "127.0.0.1";
        IpApiResult ipApiResult = new IpApiResult("localhost", "success", "United States", "US", "California", "San Francisco", "San Francisco", "94105", 37.7749, -122.4194, "America/Los_Angeles", "Internet Assigned Numbers Authority", "Internet Assigned Numbers Authority", "AS52 - University of California, San Diego");
        WeatherData weatherData = new WeatherData(new WeatherData.Coord(0.0f, 0.0f), List.of(new WeatherData.Weather(800, "Clear", "clear sky", "01d")), "stations", new WeatherData.Main(15.0f, 13.0f, 12.0f, 17.0f, 1012, 72, 1012, 1012), 10000, new WeatherData.Wind(3.09f, 170, 3.09f), new WeatherData.Clouds(0), 1525680418L, new WeatherData.Sys("GB", 1387266326L, 1387309225L), 0, 2643743, "London", 200);
        Weather savedWeather = new Weather(ip, Instant.now(), ipApiResult.lon(), ipApiResult.lat(), "Clear", "clear sky", 15.0, "United Kingdom");

        // Mocking
        when(ipApiClient.getIpApiResult(ip)).thenReturn(Mono.just(ipApiResult));
        when(openWeatherClient.getCurrentWeather(ipApiResult.lat(), ipApiResult.lon())).thenReturn(Mono.just(weatherData));
        when(weatherServiceData.saveWeatherData(ip, weatherData)).thenReturn(savedWeather);

        // Test
        Optional<WeatherDto> result = weatherServiceImpl.getWeather(ip);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(new WeatherDto("Clear", "clear sky", 15.0), result.get());
    }


    @Test
    void testGetWeatherReturnsNullWhenIpApiResultFails() {
        String ip = "127.0.0.1";
        IpApiResult ipApiResult = new IpApiResult("localhost", "fail", null, null, null, null, null, null, 0.0, 0.0, null, null, null, null);

        when(ipApiClient.getIpApiResult(ip)).thenReturn(Mono.just(ipApiResult));

        Optional<WeatherDto> result = weatherServiceImpl.getWeather(ip);

        assertTrue(result.isEmpty());
        ;
        verify(weatherServiceData, never()).saveWeatherData(any(), any());
    }


    @Test
    void testGetWeatherReturnsNullWhenWeatherDataIsNull() {
        String ip = "127.0.0.1";
        IpApiResult ipApiResult = new IpApiResult("localhost", "success", "United States", "US", "California", "San Francisco", "San Francisco", "94105", 37.7749, -122.4194, "America/Los_Angeles", "Internet Assigned Numbers Authority", "Internet Assigned Numbers Authority", "AS52 - University of California, San Diego");
        when(ipApiClient.getIpApiResult(ip)).thenReturn(Mono.just(ipApiResult));
        when(openWeatherClient.getCurrentWeather(ipApiResult.lat(), ipApiResult.lon())).thenReturn(Mono.empty());

        Optional<WeatherDto> result = weatherServiceImpl.getWeather(ip);

        assertTrue(result.isEmpty());
        verify(weatherServiceData, never()).saveWeatherData(any(), any());
    }
}


