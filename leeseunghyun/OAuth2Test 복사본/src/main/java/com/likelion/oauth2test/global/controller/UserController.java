package com.likelion.oauth2test.global.controller;

import com.likelion.oauth2test.global.dto.UserResponseDto;
import com.likelion.oauth2test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")  // "user" 테이블 대신 "users"로 경로 수정
public class UserController {
    private final UserService userService;

    // create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(
            @RequestPart("name") String name,
            @RequestPart("image") MultipartFile image) throws IOException {
        userService.createUser(name, image);
        return ResponseEntity.ok().build();
    }

    // read
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAllUser();
        return ResponseEntity.ok(users);
    }
}