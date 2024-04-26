package com.example.demo.user.controller;

import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.jwt.JwtResponseDto;
import com.example.demo.user.dto.LoginDto;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginDto dto) {
        try {
            if (userService.login(dto) == null) return ResponseEntity.status(401).body(null);
            return ResponseEntity.ok(userService.login(dto));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/nickCheck")
    public ResponseEntity<String> nickCheck(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        if (!userService.nickCheck(username)) return ResponseEntity.ok("사용하실 수 있는 닉네임입니다.");

        return ResponseEntity.status(400).body("중복된 닉네임입니다.");

    }
}
