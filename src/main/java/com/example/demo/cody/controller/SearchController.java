package com.example.demo.cody.controller;

import com.example.demo.cody.dto.ImageDto;
import com.example.demo.cody.service.NaverSearchService;
import com.example.demo.utils.NaverShopSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class SearchController {
    private final NaverShopSearch naverShopSearch;
    private final NaverSearchService naverSearchService;

        @GetMapping("/ser")
        public String naverSearch(@RequestParam String query, Model model) {
            String resultString = naverShopSearch.search(query);
            List<ImageDto> images = naverSearchService.fromJSONtoImage(resultString);
            model.addAttribute("images", images);
            return "naversearch";
        }




    }
