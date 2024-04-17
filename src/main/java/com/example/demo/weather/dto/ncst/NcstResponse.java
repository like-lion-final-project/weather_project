package com.example.demo.weather.dto.ncst;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NcstResponse {
    private NcstHeader header;
    private NcstBody body;
}
