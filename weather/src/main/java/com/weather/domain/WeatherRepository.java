package com.weather.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findByIp(String ip);
}
