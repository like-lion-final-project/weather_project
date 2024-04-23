package com.example.demo.weather.dto.srt_fcst;

import lombok.Data;

@Data
public class FcstResponse {
    private FcstHeader header;
    private FcstBody body;
}
