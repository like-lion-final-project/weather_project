package com.example.demo.weather.dto.mid_land;

import java.util.List;
import lombok.Data;

@Data
public class MidLandItems {
    private List<MidLandItem> item;
    private int pageNo;
    private int numOfRows;
    private int totalCount;
}
