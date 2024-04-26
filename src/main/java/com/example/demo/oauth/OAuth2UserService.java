package com.example.demo.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String nameAttribute = " ";

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        Map<String, Object> attributes = new HashMap<>();
        if (registrationId.equals("kakao")) {
            attributes.put("provider", "kakao");
            attributes.put("id", oAuth2User.getAttribute("id"));

            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            attributes.put("email", kakaoAccount.get("email"));
            attributes.put("name", kakaoAccount.get("name"));
            attributes.put("gender", kakaoAccount.get("gender"));
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            attributes.put("nickname", kakaoProfile.get("nickname"));
            nameAttribute = "email";
        }
        log.info(attributes.toString());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                nameAttribute
        );
    }
}
