package com.weather.services.client.openweather;

import java.util.List;

public record WeatherData(Coord coord, List<Weather> weather, String base, Main main, int visibility, Wind wind, Clouds clouds, long dt, Sys sys, int timezone, int id, String name, int cod) {


    public record Coord(double lon, double lat) {}

    public record Weather(int id, String main, String description, String icon) {}

    public record Main(double temp, float feels_like, float temp_min, float temp_max, int pressure, int humidity, int sea_level, int grnd_level) {}

    public record Wind(float speed, int deg, float gust) {}

    public record Clouds(int all) {}

    public record Sys(String country, long sunrise, long sunset) {}
}

