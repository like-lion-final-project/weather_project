package com.example.demo.weather.dto.srt_ncst;

import java.util.List;
import lombok.Data;

@Data
public class NcstItems {
    private List<NcstItem> item;
    private Integer pageNo;
    private Integer numOfRows;
    private Integer totalCount;
}
