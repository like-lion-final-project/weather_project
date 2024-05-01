package com.example.demo.weather.service.fcst;

import com.example.demo.weather.dto.mid_land.MidLandApiResponse;
import com.example.demo.weather.dto.mid_ta.MidTaApiResponse;
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
public class MidFcstService {
    private final MidFcstApiService midFcstApiService;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 중기 육상 예보
    public MidLandApiResponse getMidLandFcst(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 시간에서 06:00 이전이라면 전날 18:00 으로 설정
        if (currentTime.getHour() < 6) {
            currentTime = currentTime.minusDays(1).withHour(18).withMinute(0);
        }
        // 현재 시간이 06:00 이후이고 18:00 이전이라면 오늘 06:00 으로 설정
        else if (currentTime.getHour() < 18) {
            currentTime = currentTime.withHour(6).withMinute(0);
        }
        // 현재 시간이 18:00 이후라면 오늘 18:00 으로 설정
        else {
            currentTime = currentTime.withHour(18).withMinute(0);
        }

        String tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH00"));

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", tmFc);        // 발표 시각

        return midFcstApiService.MidLandFcst(params);
    }


    // 중기 기온 조회
    public MidTaApiResponse getMidTa(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 시간에서 06:00 이전이라면 전날 18:00 으로 설정
        if (currentTime.getHour() < 6) {
            currentTime = currentTime.minusDays(1).withHour(18).withMinute(0);
        }
        // 현재 시간이 06:00 이후이고 18:00 이전이라면 오늘 06:00 으로 설정
        else if (currentTime.getHour() < 18) {
            currentTime = currentTime.withHour(6).withMinute(0);
        }
        // 현재 시간이 18:00 이후라면 오늘 18:00 으로 설정
        else {
            currentTime = currentTime.withHour(18).withMinute(0);
        }

        String tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH00"));

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", tmFc);        // 발표 시각

        return midFcstApiService.MidTa(params);
    }
}
