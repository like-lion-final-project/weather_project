package com.example.demo.cody.controller;

import com.example.demo.cody.dto.FeedbackDto;
import com.example.demo.cody.dto.ItemDto;
import com.example.demo.cody.service.NaverSearchService;
import com.example.demo.utils.NaverShopSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class SearchController {
    private final NaverShopSearch naverShopSearch;
    private final NaverSearchService naverSearchService;


    @GetMapping("/search")
        public String naverSearch( Model model) {

            String query=naverSearchService.getCategoryTypeOfLastSuggestion();
            String resultString = naverShopSearch.search(query);
            List<ItemDto> items=naverSearchService.fromJSONtoItems(resultString);
            model.addAttribute("items" ,items);
            model.addAttribute("query",query);
            return "naversearch";
        }

    @PostMapping("/submit-rating")
    public ResponseEntity<FeedbackDto> submitRating(@RequestBody FeedbackDto feedbackDto) {
        String image = feedbackDto.getImage();
        int rating = feedbackDto.getRating();
        String category= feedbackDto.getCategory();

        System.out.println("Received image : " + image);
        System.out.println("Received rating: " + rating);
        System.out.println("Received category:  " + category);

        naverSearchService.saveFeedback(feedbackDto);
        return ResponseEntity.ok(feedbackDto);
    }

    }
