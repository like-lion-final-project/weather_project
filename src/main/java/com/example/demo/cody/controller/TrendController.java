package com.example.demo.cody.controller;


import com.example.demo.cody.dto.TrendDto;
import com.example.demo.cody.service.TrendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class TrendController {

    private final TrendService trendService;


    @GetMapping("/trends")
    public String showTrends(Model model) {
        // 실시간 트렌드 데이터를 가져옵니다.
        List<TrendDto> trends = trendService.calculateRealTimeTrends();
        // 모델에 트렌드 데이터를 담아서 HTML로 전달합니다.
        model.addAttribute("trends", trends);
        // trends.html을 반환합니다.
        return "trends";
    }



}

