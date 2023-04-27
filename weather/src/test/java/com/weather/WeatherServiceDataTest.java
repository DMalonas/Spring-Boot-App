package com.weather;
//
import com.weather.domain.Weather;
import com.weather.domain.WeatherRepository;
import com.weather.services.client.openweather.WeatherData;
import com.weather.services.weather.WeatherServiceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class WeatherServiceDataTest {

    private WeatherServiceData weatherServiceData;

    @Mock
    private WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        weatherServiceData = new WeatherServiceData(weatherRepository);
    }

    @Test
    void testSaveWeatherData() {
        String ip = "127.0.0.1";
        WeatherData weatherData = new WeatherData(new WeatherData.Coord(1.0, 2.0),
                List.of(new WeatherData.Weather(1, "Clear", "clear sky", "01n")),
                "stations",
                new WeatherData.Main(20.0, 18.0f, 21.0f, 19.0f, 1008, 78, 1008, 100),
                10000,
                new WeatherData.Wind(3.6f, 180, 4.0f),
                new WeatherData.Clouds(0),
                1622058647,
                new WeatherData.Sys("US", 1621995125, 1622049208),
                -25200,
                420006353,
                "Mountain View",
                200
        );

        Weather expectedWeather = new Weather(ip, Instant.now(), 1.0, 2.0, "Clear", "clear sky", 20.0, "US");
        weatherServiceData.saveWeatherData(ip, weatherData);

        verify(weatherRepository).save(expectedWeather);
    }
}
