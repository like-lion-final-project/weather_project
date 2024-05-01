package com.example.demo.user.repo;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);
}
