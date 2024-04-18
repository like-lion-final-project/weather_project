package com.example.demo.weather.service;

import com.example.demo.weather.dto.geolocation.GeoLocationNcpResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface NcpGeolocationService {
    @GetExchange
    GeoLocationNcpResponse geoLocation(
            @RequestParam
            Map<String, Object> params
    );
}