package com.example.demo.weather.controller;

import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.fcst.FcstApiResponse;

import com.example.demo.weather.dto.ncst.NcstApiResponse;
import com.example.demo.weather.service.NDApiService;
import com.example.demo.weather.service.NDSearchService;
import com.example.demo.weather.service.VilageSrtFcstService;
import com.example.demo.weather.service.NcpGeocodeService;

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
    private final NDSearchService ndSearchService;

    /**
     * geocode
     */
    @GetMapping("/geocode")
    public PointDto pointRegion(
            @RequestParam("query")
            String query
    ) {
        return geocodeService.getGeocode(query);
    }

    /**
     * 초단기 예보 조회 (시간대별 날씨)
     */
    @GetMapping("/by-hour")
    public FcstApiResponse getUltraSrtFcst(
            @RequestParam("nx")
            Integer nx,
            @RequestParam("ny")
            Integer ny
    ) {
        return vilageSrtFcstService.getUltraSrtFcst(nx, ny);
    }

    /**
     * 초단기 실황 조회 (현시각 날씨)
     */
    @GetMapping("/by-current")
    public NcstApiResponse getUltraSrtNcst(
            @RequestParam("nx")
            Integer nx,
            @RequestParam("ny")
            Integer ny
    ) {
        return vilageSrtFcstService.getUltraSrtNcst(nx, ny);
    }

    /**
     * 날씨 뉴스 기사 조회
     */
    @GetMapping("/news")
    public Object getWeatherNews(
            @RequestParam("start")
            Integer start
    ) {
        return ndSearchService.ndNewsSearch(start);
    }
}
