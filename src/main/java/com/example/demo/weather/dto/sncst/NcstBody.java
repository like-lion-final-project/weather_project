package com.example.demo.weather.dto.sncst;

import lombok.Data;

@Data
public class NcstBody {
    private String dataType;
    private NcstItems items;
}
