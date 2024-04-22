package com.example.demo.auth.config;

import com.example.demo.jwt.JwtTokenFilter;
import com.example.demo.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    private static final String baseUrl = "/api/v1/";

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                prependBaseUrl("users/login"),
                                prependBaseUrl("users/signup"),
                                // 테스트를 위해 임시로 permitAll()
                                prependBaseUrl("weather/**")
                        )
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.GET, prependBaseUrl("community/ootd")
                        )
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.POST, prependBaseUrl("community/ootd")
                        )
                        .authenticated()
                        .requestMatchers(
                                HttpMethod.PUT, prependBaseUrl("community/ootd")
                        )
                        .authenticated()
                        .requestMatchers(
                                HttpMethod.DELETE, prependBaseUrl("community/ootd")
                        )
                        .authenticated()
                        .requestMatchers(
                                prependBaseUrl("users/profile"),
                                prependBaseUrl("users/update"),
                                prependBaseUrl("users/delete"),
                                prependBaseUrl("cody"),
                                prependBaseUrl("cody/feedback")
                        )
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                )
                // JWT를 사용하기 때문에 보안 관련 세션 해제
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                );
        return httpSecurity.build();
    }

    private String prependBaseUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
