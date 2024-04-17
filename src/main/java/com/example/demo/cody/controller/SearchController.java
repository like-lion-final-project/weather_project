package com.example.demo.cody.controller;

import com.example.demo.cody.dto.ItemDto;
import com.example.demo.utils.NaverShopSearch;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class SearchController {
    private final NaverShopSearch naverShopSearch;

    @GetMapping("/home")
    private String main1(){
        return "home";
    }


    }
