package com.example.demo.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {
    @Value("${naver.ncp.client-id}")
    private String NCP_CLIENT_ID;

    @GetMapping("/home")
    public String home(
            @RequestParam(value = "key", required = false)
            String token,
            Model model
    ) {
        model.addAttribute("clientId", NCP_CLIENT_ID);
        model.addAttribute("token", token);
        return "home";
    }

    @GetMapping("/api/v1/users/login")
    public String login() {
        return "login";
    }

    @GetMapping("/api/v1/users/signUp")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }
}
