package com.likelion.likelionoauthlogin.user.application;

import com.likelion.likelionoauthlogin.global.dto.UserInfo;
import com.likelion.likelionoauthlogin.global.service.S3Service;
import com.likelion.likelionoauthlogin.user.api.dto.response.UserResponseDto;
import com.likelion.likelionoauthlogin.user.domain.User;
import com.likelion.likelionoauthlogin.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // read
    public UserResponseDto getUserInfo(Principal principal) {
        Long id = Long.parseLong(principal.getName());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id =" + id));

        return UserResponseDto.from(user);
    }
}
