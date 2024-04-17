package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;




@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AssistantThreadMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String messageId;
    private String object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private AssistantThread assistantThread;

    @Column(name = "run_id")
    private String runId;

    @Column(name = "role")
    private String role;


    private String type;
    private String value;
    private String file_ids;
    private String annotataions;
    private Object metadata;

}