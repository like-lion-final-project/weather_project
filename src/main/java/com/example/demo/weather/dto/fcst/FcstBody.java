package com.example.demo.weather.dto.fcst;

import java.util.List;
import lombok.Data;

@Data
public class FcstBody {
    private String dataType;
    private List<FcstItem> items;
    private int pageNo;
    private int numOfRows;
    private int totalCount;
}
