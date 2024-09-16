package com.likelion.oauth2test.user.domain.repository;

import com.likelion.oauth2test.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userEmail);
}