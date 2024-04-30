package com.example.demo.cody.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ItemDto {

    private String image;
    private String category;

    public ItemDto(JSONObject itemJson) {

        this.image = itemJson.getString("image");
        this.category=itemJson.getString("category3");
    }
}
