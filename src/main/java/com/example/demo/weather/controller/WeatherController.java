package com.example.demo.weather.controller;

import com.example.demo.weather.dto.GridDto;
import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.WeatherForecast;

import com.example.demo.weather.dto.geocoding.GeoNcpResponse;
import com.example.demo.weather.dto.ncst.NcstApiResponse;
import com.example.demo.weather.dto.news.NDNewsResponse;
import com.example.demo.weather.dto.rgeocoding.RGeoResponseDto;
import com.example.demo.weather.service.GridConversionService;
import com.example.demo.weather.service.NDSearchService;
import com.example.demo.weather.service.VilageSrtFcstService;
import com.example.demo.weather.service.NcpGeocodeService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final GridConversionService gridConversionService;

    /**
     * geocode
     */
    @GetMapping("/geocode")
    public List<PointDto> pointRegion(
            @RequestParam("query")
            String query
    ) {
        return geocodeService.getGeocode(query);
    }

    /**
     * Rgeocode
     */
    @GetMapping("/rgeocode")
    public RGeoResponseDto getAddress(
        @RequestParam("lat")
        Double lat,
        @RequestParam("lng")
        Double lng
    ) {
        return geocodeService.getAddress(lat, lng);
    }

    /**
     * 초단기 예보 조회 (시간대별 날씨)
     * 시간대별 카테고리, 값 쌍 정리 버전
     */
    @GetMapping("/by-hour")
    public List<WeatherForecast> getUltraSrtFcst(
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
    public NDNewsResponse getWeatherNews(
            @RequestParam("start")
            Integer start
    ) {
        return ndSearchService.ndNewsSearch(start);
    }

    /**
     * 위경도 좌표 정보를 격자 XY로 변환 (브라우저 geolocation 전용)
     */
    @GetMapping("/convert-grid")
    public GridDto convertToGrid(
            @RequestParam("lat")
            Double lat,
            @RequestParam("lng")
            Double lng
    ) {
        return gridConversionService.convertToGridXY(lat, lng);
    }
}
