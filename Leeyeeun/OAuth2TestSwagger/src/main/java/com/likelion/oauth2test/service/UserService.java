package com.likelion.oauth2test.service;

import com.likelion.oauth2test.user.api.dto.UserResponseDto;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto getUserInfo(Principal principal) {
        Long id = Long.parseLong(principal.getName());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id =" + id));
        return UserResponseDto.from(user);
    }
}