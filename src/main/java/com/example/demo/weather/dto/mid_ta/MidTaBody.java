package com.example.demo.weather.dto.mid_ta;

import lombok.Data;

@Data
public class MidTaBody {
    private String dataType;
    private MidTaItems items;
    private int pageNo;
    private int numOfRows;
    private int totalCount;
}
