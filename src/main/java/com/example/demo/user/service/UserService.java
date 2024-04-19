package com.example.demo.user.service;

import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final AuthenticationFacade authFacade;

    public UserDto createUser(UserDto dto) {
        return UserDto.fromEntity(userRepo.save(User.builder()
                    .uuid(UUID.randomUUID().toString())
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .email(dto.getEmail())
                    .gender(dto.getGender())
                    .role_id(dto.getRole_id())
                    .achievement(dto.getAchievement())
                    .build())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public boolean userExists(String email) {
        return userRepo.existsByEmail(email);
    }
}
