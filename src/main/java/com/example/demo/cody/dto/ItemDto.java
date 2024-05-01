package com.example.demo.cody.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ItemDto {

    private String image;
    private String category1;
    private String category2;
    private String category3;
    private String category4;

    public ItemDto(JSONObject itemJson) {

        this.image = itemJson.getString("image");
        this.category1=itemJson.getString("category1");
        this.category2=itemJson.getString("category2");
        this.category3=itemJson.getString("category3");
        this.category4=itemJson.getString("category4");


    }
}
