package com.example.demo.weather.dto.sfcst;

import lombok.Data;

@Data
public class FcstResponse {
    private FcstHeader header;
    private FcstBody body;
}
