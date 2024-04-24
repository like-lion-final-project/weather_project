package com.example.demo.weather.dto.srt_ncst;

import lombok.Data;

@Data
public class NcstBody {
    private String dataType;
    private NcstItems items;
}
