package com.weather.services.client.ipapi;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
public class IpApiClient {

    private final WebClient webClient;

    public IpApiClient(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl("http://ip-api.com/json/")
                .build();
    }

    public IpApiClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://ip-api.com/json/")
                .build();
    }

    public Mono<IpApiResult> getIpApiResult(String query) {
        return webClient.get()
                .uri(query)
                .retrieve()
                .bodyToMono(IpApiResult.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .onErrorResume(error -> {
                    // handle error here
                    System.out.println("Error occurred: " + error.getMessage());
                    return Mono.empty();
                });
    }
}

