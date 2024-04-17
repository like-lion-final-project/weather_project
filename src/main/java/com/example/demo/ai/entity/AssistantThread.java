package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AssistantThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    //    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Assistant assistant;
    private String name;



}
