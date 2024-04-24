package com.example.demo.cody.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFeedbackCountDto {
    private String category;
    private Long feedbackCount;
}
