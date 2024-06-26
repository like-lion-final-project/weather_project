package com.example.demo.cody.controller;


import com.example.demo.cody.dto.CategoryFeedbackCountDto;
import com.example.demo.cody.dto.TrendDto;
import com.example.demo.cody.service.TrendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

@Controller
@AllArgsConstructor
public class TrendController {

    private final TrendService trendService;


    @GetMapping("/trends")
    public String showTrends(Model model) throws JsonProcessingException {
        List<TrendDto> trends = trendService.calculateRealTimeTrends();
        List<CategoryFeedbackCountDto> feedbackCountsByCategory = trendService.countFeedbacksByCategory();
        ObjectMapper objectMapper = new ObjectMapper();
        String trendsJson = objectMapper.writeValueAsString(trends);
        String feedbackJson = objectMapper.writeValueAsString(feedbackCountsByCategory);
        model.addAttribute("trends", trendsJson);
        model.addAttribute("feedback",feedbackJson);
        return "trends";
    }



}

