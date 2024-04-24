package com.example.demo.weather.service.ncp;

import com.example.demo.weather.dto.geocoding.GeoNcpResponse;

import com.example.demo.weather.dto.rgeocoding.RGeoNcpResponse;
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

    @GetExchange("/map-reversegeocode/v2/gc")
    RGeoNcpResponse reverseGeocode(
            @RequestParam
            Map<String, Object> params
    );
}
