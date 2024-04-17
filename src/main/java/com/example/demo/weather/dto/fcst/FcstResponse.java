package com.example.demo.weather.dto.fcst;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FcstResponse {
    private FcstHeader header;
    private FcstBody body;
}
