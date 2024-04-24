package com.example.demo.cody.dto;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class ImageDto {
    private String image;

    public ImageDto(JSONObject imageJson) {
        this.image = imageJson.getString("image");

    }
}
