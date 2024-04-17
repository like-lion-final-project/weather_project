package com.example.demo.weather.dto.fcst;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FcstResponse {
    private FcstHeader header;
    private FcstBody body;
}
