package com.example.demo.jwt;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
}
