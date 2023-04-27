package com.weather;


import com.weather.controllers.WeatherController;
import com.weather.services.dto.WeatherDto;
import com.weather.services.weather.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetWeather() throws Exception {
        WeatherDto weatherDto = new WeatherDto("Clear", "Clear sky", 25.0);
        given(weatherService.getWeather("1.2.3.4")).willReturn(Optional.of(weatherDto));

        mockMvc.perform(get("/weather").param("ip", "1.2.3.4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.main").value("Clear"))
                .andExpect(jsonPath("$.description").value("Clear sky"))
                .andExpect(jsonPath("$.temp").value(25.0));
    }

    @Test
    public void testGetWeatherNotFound() throws Exception {
        given(weatherService.getWeather("1.2.3.4")).willReturn(Optional.empty());

        mockMvc.perform(get("/weather").param("ip", "1.2.3.4"))
                .andExpect(status().isNotFound());
    }
}
