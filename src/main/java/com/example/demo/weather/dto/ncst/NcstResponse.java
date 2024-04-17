package com.example.demo.weather.dto.ncst;

import lombok.Data;

@Data
public class NcstResponse {
    private NcstHeader header;
    private NcstBody body;
}