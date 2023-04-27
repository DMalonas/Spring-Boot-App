package com.weather.services.client.openweather;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OpenWeatherClient {
    private final WebClient webClient;
    private OpenWeatherProperties openWeatherProperties;

    public OpenWeatherClient(OpenWeatherProperties openWeatherProperties) {
        this.openWeatherProperties = openWeatherProperties;
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                ))
                .baseUrl("https://api.openweathermap.org/data/2.5/weather")
                .build();
    }
//    Bad return type in lambda expression: UriBuilder cannot be converted to URI
    public Mono<WeatherData> getCurrentWeather(double lat, double lon) {
        return webClient.get()
                .uri(builder -> builder
                        .queryParam("lat", String.valueOf(lat))
                        .queryParam("lon", String.valueOf(lon))
                        .queryParam("appid", String.valueOf(this.openWeatherProperties.getKey()))
                        .build())
                .retrieve()
                .bodyToMono(WeatherData.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .onErrorResume(error -> {
                    // handle error here
                    System.out.println("Error occurred: " + error.getMessage());
                    return Mono.empty();
                });
    }

}
