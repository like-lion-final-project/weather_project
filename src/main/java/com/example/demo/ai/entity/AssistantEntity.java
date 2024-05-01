package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "assistant")
public class AssistantEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // fashion, ... 등등 어시스턴트의 역할을 표현할 수 있는 타입
    private String assistantType;
    @Column(columnDefinition = "TEXT")
    private String instructions;
    private String name;

    // 0.0.1 같은 버전을 의미함
    private String version;

    // lastest 등 버전의 라벨을 의미함
    private String versionLabel;
    private String model;
    @Column(name = "assistant_unique_id")
    private String assistantId;
    private boolean isDeleteFromOpenAi;

}
