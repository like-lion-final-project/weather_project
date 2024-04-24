package com.example.demo.weather.dto.srt_ncst;

import lombok.Data;

@Data
public class NcstItem {
    private String baseDate;
    private String baseTime;
    private String category;
    private Integer nx;
    private Integer ny;
    private Double obsrValue;
}
