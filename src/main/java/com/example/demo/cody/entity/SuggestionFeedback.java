package com.example.demo.cody.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String image;
    private String query;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinColumn(name = "category_Id", referencedColumnName = "id")
    private ClothsCategory category;
}
