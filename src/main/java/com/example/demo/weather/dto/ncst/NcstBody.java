package com.example.demo.weather.dto.ncst;

import lombok.Data;

@Data
public class NcstBody {
    private String dataType;
    private NcstItems items;
}
