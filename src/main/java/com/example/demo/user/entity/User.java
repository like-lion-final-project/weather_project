package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    @Column(nullable = false)
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String email;
    @Setter
    private Integer gender; // 0: 여자, 1: 남자
    @Setter
    private Integer role_id;
    @Setter
    private String achievement;
}
