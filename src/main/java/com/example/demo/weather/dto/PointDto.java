package com.example.demo.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointDto {
    private String roadAddress;
    private Double lat;
    private Double lng;

    public PointDto(Double lat, Double lng) {
        this.roadAddress = null;
        this.lat = lat;
        this.lng = lng;
    }
}
