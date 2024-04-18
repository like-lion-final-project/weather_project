package com.example.demo.weather.service;

import com.example.demo.weather.dto.fcst.FcstApiResponse;
import com.example.demo.weather.dto.ncst.NcstApiResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VilageSrtFcstService {
    private final VilageFcstApiService vilageFcstApiService;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 초단기 예보 (Short-Term Forecast)
    public FcstApiResponse getUltraSrtFcst(
            Integer nx,
            Integer ny
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        int minutes = currentTime.getMinute();

        // 현재 시각에서 가장 가까운 과거의 30분 계산
        if (minutes < 30) {
            currentTime = currentTime.minusHours(1).withMinute(30);
        } else {
            currentTime = currentTime.withMinute(0);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 60);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("base_date", dateTimeFormatter.format(currentTime));  // 발표 일자
        params.put("base_time", timeFormatter.format(currentTime));      // 발표 시각 (1시간전 기준)
        params.put("nx", nx);                                            // 예보지점 x좌표 값
        params.put("ny", ny);                                            // 예보지점 y좌표 값

        return vilageFcstApiService.UltraSrtFcst(params);
    }


    // 초단기 실황 (Short-Term Nowcast)
    public NcstApiResponse getUltraSrtNcst(
            Integer nx,
            Integer ny
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 8);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("base_date", dateTimeFormatter.format(currentTime));  // 발표 일자
        params.put("base_time", timeFormatter.format(currentTime));      // 발표 시각
        params.put("nx", nx);                                            // 예보지점 x좌표 값
        params.put("ny", ny);                                            // 예보지점 y좌표 값

        return vilageFcstApiService.UltraSrtNcst(params);
    }
}
