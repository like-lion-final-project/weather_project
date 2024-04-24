package com.example.demo.weather.dto.srt_fcst;

import java.util.List;
import lombok.Data;

@Data
public class FcstItems {
    private List<FcstItem> item;
    private Integer pageNo;
    private Integer numOfRows;
    private Integer totalCount;
}
