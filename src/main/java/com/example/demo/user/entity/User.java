package com.example.demo.user.entity;

import com.example.demo.community.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Setter
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> post;
}
