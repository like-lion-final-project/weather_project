package com.example.demo.cody.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    @OneToMany(mappedBy = "category")
    private List<SuggestionFeedback>suggestionFeedbacks=new ArrayList<>();
    @OneToMany(mappedBy = "category")
    private List<DailySuggestion>dailySuggestions=new ArrayList<>();
}
