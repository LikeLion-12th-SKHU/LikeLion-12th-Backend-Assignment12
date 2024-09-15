package com.likelion.oauth2test.user.domain.repository;

import com.likelion.oauth2test.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String userEmail);
}