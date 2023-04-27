package com.weather.services.weather;

import com.weather.domain.Weather;
import com.weather.services.client.ipapi.IpApiClient;
import com.weather.services.client.ipapi.IpApiResult;
import com.weather.services.client.openweather.OpenWeatherClient;
import com.weather.services.client.openweather.WeatherData;
import com.weather.services.dto.WeatherDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService{

    private final IpApiClient ipApiClient;
    private final OpenWeatherClient openWeatherClient;

    private final WeatherServiceData weatherServiceData;

    public WeatherServiceImpl(IpApiClient ipApiClient,
                              OpenWeatherClient openWeatherClient, WeatherServiceData weatherServiceData) {
        this.ipApiClient = ipApiClient;
        this.openWeatherClient = openWeatherClient;
        this.weatherServiceData = weatherServiceData;
    }


    @Override
    @Cacheable(value = "ip", key="'weather' + #ip.toString()")
    public Optional<WeatherDto> getWeather(String ip) {
        IpApiResult result = getIpApiResult(ip);
        if (result.status().equals("fail")) {
            return Optional.empty();
        }
        Mono<WeatherData> currentWeather = openWeatherClient.getCurrentWeather(result.lat(), result.lon());
        WeatherData weatherData = currentWeather.block();
        if (weatherData == null) {
            return Optional.empty();
        }
        //Save to DB
        Weather savedWeather = weatherServiceData.saveWeatherData(ip, weatherData);
        //Create and return DTO
        WeatherDto weatherDto = new WeatherDto(savedWeather.getMain(), savedWeather.getDescription(), savedWeather.getTemp());
        return Optional.of(weatherDto);
    }


    private IpApiResult getIpApiResult(String ip) {
        Mono<IpApiResult> ipApiResult = this.ipApiClient.getIpApiResult(ip);
        IpApiResult result = ipApiResult.block();
        return result;
    }

}
