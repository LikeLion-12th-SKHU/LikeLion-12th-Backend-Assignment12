package com.likelion.oauth2test.service;

import com.likelion.oauth2test.global.dto.UserResponseDto;
import com.likelion.oauth2test.user.domain.Users;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    // create
    @Transactional
    public void createUser(String name, MultipartFile userImage) throws IOException {
        String imageUrl = s3Service.upload(userImage, "user");
        Users user = Users.builder()
                .name(name)
                .pictureUrl(imageUrl) // 필드 이름 수정
                .build();
        userRepository.save(user);
    }

    // read
    public List<UserResponseDto> findAllUser() {
        List<Users> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (Users user : userList) {
            userResponseDtoList.add(UserResponseDto.of(user.getId(), user.getName(), user.getPictureUrl())); // 메서드 이름 수정
        }
        return userResponseDtoList;
    }

}
