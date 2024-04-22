package com.example.demo.weather.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class WeatherForecast {
    private String fcstTime;
    private Map<String, String> forecastValues;

    public WeatherForecast(String fcstTime) {
        this.fcstTime = fcstTime;
        this.forecastValues = new HashMap<>();
    }

    public void addForecastValue(String category, String value) {
        forecastValues.put(category, value);
    }
}
