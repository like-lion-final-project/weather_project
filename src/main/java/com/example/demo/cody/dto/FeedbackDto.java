package com.example.demo.cody.dto;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.json.JSONObject;

@Getter
@Setter
public class FeedbackDto {

    private int rating;
    private String image;
    private String category;

}
