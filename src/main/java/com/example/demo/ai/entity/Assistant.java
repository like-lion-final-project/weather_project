package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Assistant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String instructions;
    private String name;
    private String version;
    private String model;
    @Column(name = "assistant_unique_id")
    private String assistantId;
    private boolean isDeleteFromOpenAi;
}
