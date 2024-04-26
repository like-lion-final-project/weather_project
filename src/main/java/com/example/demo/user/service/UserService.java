package com.example.demo.user.service;

import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.jwt.JwtResponseDto;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.user.dto.LoginDto;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.CustomUserDetails;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public UserDto createUser(UserDto dto) {
        return UserDto.fromEntity(userRepo.save(User.builder()
                    .uuid(UUID.randomUUID().toString())
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .gender(dto.getGender())
                    .role_id(dto.getRole_id())
                    .achievement(dto.getAchievement())
                    .build())
        );
    }

    public JwtResponseDto login(LoginDto dto) {
        String token = "";
        User user = userRepo.findUserByEmail(dto.getEmail()).get();

        log.info("디비 아이디: " + user.getEmail());
        log.info("로그인 아이디: " + dto.getEmail());
        log.info("비밀번호 일치여부: " + passwordEncoder.matches(dto.getPassword(), user.getPassword()));

        if (!user.getEmail().equals(dto.getEmail())) {
            return null;
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return null;
        }

        token = jwtTokenUtils.generateToken(userRepo.findUserByEmail(dto.getEmail()).get());

        return JwtResponseDto.builder().token(token).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepo.findUserByUsername(username);
        if (optUser.isEmpty()) throw new UsernameNotFoundException("username not found");
        User user = optUser.get();

        return CustomUserDetails.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .gender(user.getGender())
                .role_id(user.getRole_id())
                .achievement(user.getAchievement())
                .build();
    }

    public boolean userExists(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean nickCheck(String username) {
        return userRepo.existsByUsername(username);
    }
}
