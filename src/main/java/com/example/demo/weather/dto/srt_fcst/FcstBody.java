package com.example.demo.weather.dto.srt_fcst;

import lombok.Data;

@Data
public class FcstBody {
    private String dataType;
    private FcstItems items;
}
