package com.example.demo.weather.service;

import com.example.demo.weather.dto.geocoding.GeoNcpResponse;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface NcpMapApiService {

    // Geocode
    @GetExchange("/map-geocode/v2/geocode")
    GeoNcpResponse geocode(
            @RequestParam
            Map<String, Object> params
    );

}
