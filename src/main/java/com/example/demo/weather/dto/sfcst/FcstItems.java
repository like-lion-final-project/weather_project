package com.example.demo.weather.dto.sfcst;

import java.util.List;
import lombok.Data;

@Data
public class FcstItems {
    private List<FcstItem> item;
    private Integer pageNo;
    private Integer numOfRows;
    private Integer totalCount;
}
