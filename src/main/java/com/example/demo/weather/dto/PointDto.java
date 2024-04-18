package com.example.demo.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointDto {
    private Double lat;
    private Double lng;
}
