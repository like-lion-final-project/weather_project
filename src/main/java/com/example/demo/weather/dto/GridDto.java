package com.example.demo.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GridDto {
    // 격자 XY
    private Integer nx;
    private Integer ny;

    public String toQueryValue() {
        return String.format("%d,%d", nx, ny);
    }
}
