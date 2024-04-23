package com.example.demo.weather.service;

import com.example.demo.weather.dto.WeatherForecast;
import com.example.demo.weather.dto.WeatherNowcast;
import com.example.demo.weather.dto.srt_fcst.FcstApiResponse;
import com.example.demo.weather.dto.srt_fcst.FcstItem;
import com.example.demo.weather.dto.srt_ncst.NcstApiResponse;
import com.example.demo.weather.dto.srt_ncst.NcstItem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SrtFcstService {
    private final SrtFcstApiService srtFcstApiService;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 초단기 예보 (Short-Term Forecast)
    public List<WeatherForecast> getUltraSrtFcst(
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

        // API 응답 받기
        FcstApiResponse response = srtFcstApiService.UltraSrtFcst(params);

        // 각 fcstTime에 대한 forecastValues를 하나의 맵으로 합치기
        List<WeatherForecast> forecasts = new ArrayList<>();
        Map<String, WeatherForecast> forecastMap = new HashMap<>();
        for (FcstItem item : response.getResponse().getBody().getItems().getItem()) {
            String fcstTime = item.getFcstTime();
            WeatherForecast weatherForecast = forecastMap.get(fcstTime);
            if (weatherForecast == null) {
                weatherForecast = new WeatherForecast(fcstTime);
                forecastMap.put(fcstTime, weatherForecast);
                forecasts.add(weatherForecast);
            }
            weatherForecast.addForecastValue(item.getCategory(), item.getFcstValue());
        }

        return forecasts;


    }


    // 초단기 실황 (Short-Term Nowcast)
    public WeatherNowcast getUltraSrtNcst(
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
        params.put("numOfRows", 8);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("base_date", dateTimeFormatter.format(currentTime));  // 발표 일자
        params.put("base_time", timeFormatter.format(currentTime));      // 발표 시각
        params.put("nx", nx);                                            // 예보지점 x좌표 값
        params.put("ny", ny);                                            // 예보지점 y좌표 값

        // API 응답 받기
        NcstApiResponse response = srtFcstApiService.UltraSrtNcst(params);

        String baseDate = response.getResponse().getBody().getItems().getItem().get(0).getBaseDate();
        String baseTime = response.getResponse().getBody().getItems().getItem().get(0).getBaseTime();
        Integer ncstNx = response.getResponse().getBody().getItems().getItem().get(0).getNx();
        Integer ncstNy = response.getResponse().getBody().getItems().getItem().get(0).getNy();

        WeatherNowcast weatherNowcast = new WeatherNowcast(baseDate, baseTime, ncstNx, ncstNy);
        for (NcstItem item : response.getResponse().getBody().getItems().getItem()) {
            weatherNowcast.addNowcastValue(item.getCategory(), item.getObsrValue());
        }

        return weatherNowcast;
    }
}
