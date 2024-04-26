package com.example.demo.auth.config;

import com.example.demo.jwt.JwtTokenFilter;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.oauth.OAuth2SuccessHandler;
import com.example.demo.oauth.OAuth2UserService;
import com.example.demo.user.service.UserService;
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
    private final UserService userService;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

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
                                prependBaseUrl("weather/**"),
                                prependBaseUrl("area-code/**")
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
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage(prependBaseUrl("users/login"))
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                userService
                        ),
                        AuthorizationFilter.class
                );
        return httpSecurity.build();
    }

    private String prependBaseUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
