package com.example.demo.cody.controller;

import com.example.demo.cody.dto.ItemDto;
import com.example.demo.cody.service.NaverSearchService;
import com.example.demo.utils.NaverShopSearch;
import lombok.AllArgsConstructor;
import com.example.demo.utils.NaverShopSearch;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class SearchRequestController {

    private final NaverShopSearch naverShopSearch;
    private final NaverSearchService naverSearchService;

    @GetMapping("/search")
    public List<ItemDto> getItems(@RequestParam String query) {
        String resultString = naverShopSearch.search(query);
        return naverSearchService.fromJSONtoItems(resultString);
    }
}


