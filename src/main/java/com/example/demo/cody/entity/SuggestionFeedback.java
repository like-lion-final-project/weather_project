package com.example.demo.cody.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionFeedback {
    @Id
    private int id;
    private int rating;
    private LocalDateTime create_at;
    private LocalDateTime update_at;
}
