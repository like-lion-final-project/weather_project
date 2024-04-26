package com.example.demo.oauth;

import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.CustomUserDetails;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsManager userDetailsManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String username = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String gender = oAuth2User.getAttribute("gender");

        if (!userService.userExists(email)) {
            userService.createUser(UserDto.builder()
                    .uuid(UUID.randomUUID().toString())
                    .username(username)
                    .email(email)
                    .gender(gender == "male" ? 1 : 0)
                    .build());
        }

        UserDetails details = userDetailsManager.loadUserByUsername(username);
        User user = new User();
        String jwt = tokenUtils.generateToken(user);

        String targetUrl = String.format("http://localhost:8080/api/v1/users/login?token=%s", jwt);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
