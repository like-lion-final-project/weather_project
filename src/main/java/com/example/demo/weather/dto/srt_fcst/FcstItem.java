package com.example.demo.weather.dto.srt_fcst;

import lombok.Data;

@Data
public class FcstItem {
    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private Integer nx;
    private Integer ny;
}
