package com.example.demo.user.controller;

import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationFacade authFacade;

    @GetMapping("/login")
    public String login(UserDto dto) {
        return "login";
    }

    @GetMapping("/home")
    public String main() {
        return "home.html";
    }
}
