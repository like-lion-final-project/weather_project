package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;


// 테스트용 임시 엔티티 입니다. User 엔티티 생성완료시 삭제 해주세요
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
