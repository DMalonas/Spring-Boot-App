package com.weather;


import com.weather.domain.Weather;
import com.weather.domain.WeatherRepository;
import com.weather.services.client.ipapi.IpApiClient;
import com.weather.services.client.ipapi.IpApiResult;
import com.weather.services.client.openweather.OpenWeatherClient;
import com.weather.services.client.openweather.WeatherData;
import com.weather.services.client.openweather.WeatherData.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Transactional
public class WeatherControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IpApiClient ipApiClient;

    @MockBean
    private OpenWeatherClient openWeatherClient;

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    public void testGetWeather() {
        String ip = "127.0.0.1";
        IpApiResult ipApiResult = new IpApiResult(
                ip,
                "success",
                "United States",
                "US",
                "California",
                "California",
                "Mountain View",
                "94040",
                37.386,
                -122.0838,
                "America/Los_Angeles",
                "Google LLC",
                "Google LLC",
                "AS15169 Google LLC"
        );
        when(ipApiClient.getIpApiResult(ip)).thenReturn(Mono.just(ipApiResult));
        WeatherData weatherData = new WeatherData(
                new Coord(-122.0838, 37.386),
                List.of(new WeatherData.Weather(800, "Clear", "clear sky",  "01d")),
                "stations",
                new Main(293.79, 281.72F, 284.19F, 1012, 22, 14, 1020,  878),
                10000,
                new WeatherData.Wind(3.6f, 0, 0),
                new WeatherData.Clouds(1),
                1630095355L,
                new WeatherData.Sys("US", 1630058562, 1630104681),
                -25200,
                420006353,
                "Mountain View",
                200
                );
        when(openWeatherClient.getCurrentWeather(37.386, -122.0838)).thenReturn(Mono.just(weatherData));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/weather").queryParam("ip", ip).build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.main").isEqualTo("Clear")
                .jsonPath("$.description").isEqualTo("clear sky")
                .jsonPath("$.temp").isEqualTo(293.79);

        Weather savedWeather = weatherRepository.findByIp(ip).orElse(null);
        assertNotNull(savedWeather);
        assertEquals("Clear", savedWeather.getMain());
        assertEquals("clear sky", savedWeather.getDescription());
        assertEquals(293.79, savedWeather.getTemp(), 0.001);
    }
}


