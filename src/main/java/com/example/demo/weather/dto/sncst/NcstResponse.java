package com.example.demo.weather.dto.sncst;

import lombok.Data;

@Data
public class NcstResponse {
    private NcstHeader header;
    private NcstBody body;
}