package com.example.demo.weather.dto.ncst;

import lombok.Data;

@Data
public class NcstItem {
    private String baseDate;
    private String baseTime;
    private String category;
    private int nx;
    private int ny;
    private String obsrValue;
}
