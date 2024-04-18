package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String instructions;
    private String name;
    private String version;
    @Column(name = "assistant_unique_id")
    private String assistantId;

    @Column(name = "is_active")
    private Boolean isActive;
}
