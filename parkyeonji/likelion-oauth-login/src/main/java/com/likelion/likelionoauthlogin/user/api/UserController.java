package com.likelion.likelionoauthlogin.user.api;

import com.likelion.likelionoauthlogin.user.api.dto.response.UserResponseDto;
import com.likelion.likelionoauthlogin.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // read
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserInfo(Principal principal) {
        return new ResponseEntity<>(userService.getUserInfo(principal), HttpStatus.OK);
    }
}
