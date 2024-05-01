package com.example.demo.weather.controller;

import com.example.demo.weather.dto.GridDto;
import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.WeatherForecast;

import com.example.demo.weather.dto.WeatherNowcast;
import com.example.demo.weather.dto.mid_land.MidLandApiResponse;
import com.example.demo.weather.dto.mid_ta.MidTaApiResponse;
import com.example.demo.weather.dto.news.NDNewsResponse;
import com.example.demo.weather.dto.rgeocoding.RGeoResponseDto;
import com.example.demo.weather.service.ncp.GridConversionService;
import com.example.demo.weather.service.fcst.MidFcstService;
import com.example.demo.weather.service.nd.NDSearchService;
import com.example.demo.weather.service.fcst.SrtFcstService;
import com.example.demo.weather.service.ncp.NcpGeocodeService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final SrtFcstService srtFcstService;
    private final MidFcstService midFcstService;
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
        return srtFcstService.getUltraSrtFcst(nx, ny);
    }

    /**
     * 초단기 실황 조회 (현시각 날씨)
     */
    @GetMapping("/by-current")
    public WeatherNowcast getUltraSrtNcst(
            @RequestParam("nx")
            Integer nx,
            @RequestParam("ny")
            Integer ny
    ) {
        return srtFcstService.getUltraSrtNcst(nx, ny);
    }

    /**
     * 중기 육상 예보 조회
     */
    @GetMapping("/mid-land")
    public MidLandApiResponse getMidLandFcst(
            @RequestParam("regId")
            String regId
    ) {
        return midFcstService.getMidLandFcst(regId);
    }

    /**
     * 중기 기온 조회
     */
    @GetMapping("/mid-ta")
    public MidTaApiResponse getMidTa(
            @RequestParam("regId")
            String regId
    ) {
        return midFcstService.getMidTa(regId);
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
