package com.example.demo.cody.service;

import com.example.demo.cody.dto.ImageDto;
import com.example.demo.cody.dto.ItemDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NaverSearchService {
    public List<ItemDto> fromJSONtoItems(String result) {
        JSONObject rjson = new JSONObject(result);
        System.out.println(rjson);
        JSONArray items = rjson.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            ItemDto itemDto = new ItemDto(itemJson);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    public List<ImageDto> fromJSONtoImage(String result) {
        JSONObject rjson = new JSONObject(result);
        System.out.println(rjson);
        JSONArray images = rjson.getJSONArray("items");
        List<ImageDto> imagetdoList = new ArrayList<>();
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageJson = images.getJSONObject(i);
            ImageDto imageDto = new ImageDto(imageJson);
            imagetdoList.add(imageDto);
        }
        return imagetdoList;
    }
}
