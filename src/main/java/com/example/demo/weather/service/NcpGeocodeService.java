package com.example.demo.weather.service;

import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.geocoding.GeoNcpResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NcpGeocodeService {
    private final NcpMapApiService ncpMapApiService;

    public PointDto getGeocode(
            String query
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);         // 주소
        // params.put("coordinate", "55,127");  // 검색 중심 좌표
        params.put("page", 1);
        params.put("count", 1);

        GeoNcpResponse response = ncpMapApiService.geocode(params);
        Double lat = Double.valueOf(response.getAddresses().get(0).getY());
        Double lng = Double.valueOf(response.getAddresses().get(0).getX());

        return new PointDto(lat, lng);
    }

}
