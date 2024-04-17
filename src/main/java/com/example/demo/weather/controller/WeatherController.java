package com.example.demo.weather.controller;

import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.geolocation.GeoLocationNcpResponse;
import com.example.demo.weather.service.VilageSrtFcstService;
import com.example.demo.weather.service.NcpGeocodeService;
import com.example.demo.weather.service.NcpGeolocationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final VilageSrtFcstService vilageSrtFcstService;
    private final NcpGeocodeService geocodeService;
    private final NcpGeolocationService geolocationService;

    // (nx, ny) 위치의 날씨 조회 테스트 (시간 : 4/16 23:00)
    @GetMapping
    public Object weatherFcst(
            @RequestParam("nx")
            Double nx,
            @RequestParam("ny")
            Double ny
    ) {
        return vilageSrtFcstService.getVilageFcst(nx, ny);
    }

    // geocode
    @GetMapping("/geocode")
    public PointDto pointRegion(
            @RequestParam("query")
            String query
    ) {
        return geocodeService.getGeocode(query);
    }

    // geolocation
    @GetMapping("/geolocation")
    public GeoLocationNcpResponse geoLocation(
            @RequestParam("ip")
            String ip
    ) {
        return geolocationService.geoLocation(Map.of(
                "ip", ip,
                "responseFormatType", "json",
                "ext", "t"
        ));
    }
}
