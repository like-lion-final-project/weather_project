package com.example.demo.weather.dto.rgeocoding;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RGeoNcpResponse {
    private Map<String, String> status;
    private List<RGeoResult> results;
}
