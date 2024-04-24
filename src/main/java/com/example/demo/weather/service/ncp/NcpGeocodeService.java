package com.example.demo.weather.service.ncp;

import com.example.demo.weather.dto.PointDto;
import com.example.demo.weather.dto.geocoding.GeoAddress;
import com.example.demo.weather.dto.geocoding.GeoNcpResponse;
import com.example.demo.weather.dto.rgeocoding.RGeoNcpResponse;
import com.example.demo.weather.dto.rgeocoding.RGeoRegion;
import com.example.demo.weather.dto.rgeocoding.RGeoResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NcpGeocodeService {
    private final NcpMapApiService ncpMapApiService;
    private final GridConversionService gridConversionService;

    public List<PointDto> getGeocode(
            String query
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("page", 1);
        params.put("count", 10);

        GeoNcpResponse response = ncpMapApiService.geocode(params);
        List<PointDto> pointDtoList = new ArrayList<>();

        for (GeoAddress address : response.getAddresses()) {
            String roadAddress = address.getRoadAddress();
            Double lat = Double.valueOf(address.getY());
            Double lng = Double.valueOf(address.getX());
            PointDto pointDto = new PointDto(roadAddress, lat, lng);
            pointDtoList.add(pointDto);
        }

        return pointDtoList;
    }

    /*
    * // 좌표를 격자 좌표로 변환
            GridDto gridDto = gridConversionService.convertToGridXY(lat, lng);
    * */

    public RGeoResponseDto getAddress(Double lat, Double lng) {
        Map<String, Object> params = new HashMap<>();
        params.put("coords", String.format("%f,%f", lng, lat));
        params.put("output", "json");
        RGeoNcpResponse response = ncpMapApiService.reverseGeocode(params);
        RGeoRegion region = response.getResults()
                .get(0)
                .getRegion();

        String address = region.getArea0().getName() + " " +
                region.getArea1().getName() + " " +
                region.getArea2().getName() + " " +
                region.getArea3().getName() + " " +
                region.getArea4().getName();
        return new RGeoResponseDto(address.trim());
    }

}
