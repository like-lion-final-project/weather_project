package com.example.demo.weather.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class WeatherNowcast {
    private String baseDate;
    private String baseTime;
    private Integer nx;
    private Integer ny;
    private Map<String, Double> nowcastValue;

    public WeatherNowcast(
            String baseDate,
            String baseTime,
            Integer nx,
            Integer ny
    ) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
        this.nowcastValue = new HashMap<>();
    }

    public void addNowcastValue(
            String category,
            Double obsrValue
    ) {
        nowcastValue.put(category, obsrValue);
    }
}
