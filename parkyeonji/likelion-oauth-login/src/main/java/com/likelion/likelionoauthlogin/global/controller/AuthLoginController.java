package com.likelion.likelionoauthlogin.global.controller;

import com.likelion.likelionoauthlogin.global.dto.Token;
import com.likelion.likelionoauthlogin.global.service.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;

    @GetMapping("/code/google") // 인가코드를 받음
    public Token googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignUp(googleAccessToken);
    }

    // 인가코드를 통해 로그인이나 회원가입하도록 함
    public Token loginOrSignUp(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}
