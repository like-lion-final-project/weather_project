package com.example.demo.user.dto;

import com.example.demo.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String uuid;
    private String username;
    private String password;
    private String email;
    private Integer gender;
    private Integer role_id;
    private String achievement;

    public static UserDto fromEntity(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .role_id(entity.getRole_id())
                .achievement(entity.getAchievement())
                .build();
    }
}
