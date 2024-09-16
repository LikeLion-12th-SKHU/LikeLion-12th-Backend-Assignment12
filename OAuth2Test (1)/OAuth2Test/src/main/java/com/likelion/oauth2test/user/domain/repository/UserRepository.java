package com.likelion.oauth2test.user.domain.repository;

import com.likelion.oauth2test.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 추후 사용자 정보 db에 저장할 때 사용
    Optional<User> findByEmail(String userEmail);
}