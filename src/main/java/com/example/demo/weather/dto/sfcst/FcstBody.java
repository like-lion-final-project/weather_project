package com.example.demo.weather.dto.sfcst;

import lombok.Data;

@Data
public class FcstBody {
    private String dataType;
    private FcstItems items;
}
