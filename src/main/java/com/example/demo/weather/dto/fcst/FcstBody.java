package com.example.demo.weather.dto.fcst;

import java.util.List;
import lombok.Data;

@Data
public class FcstBody {
    private String dataType;
    private FcstItems items;
}
