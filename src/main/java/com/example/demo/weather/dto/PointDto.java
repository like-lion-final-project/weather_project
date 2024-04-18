package com.example.demo.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointDto {
    // 위경도 좌표
    private Double lat;
    private Double lng;

    // 격자 XY
    private Integer nx;
    private Integer ny;
}
