package com.likelion.oauth2test.global.controller;

import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.service.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;

    /* 콘솔에 출력(테스트용)
    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        authLoginService.socialLogin(code, registrationId);
    }*/
    @GetMapping("/code/google") // 인가 코드를 받음
    public Token googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignUp(googleAccessToken);
    }

    // 인가코드를 통해 로그인이나 회원가입하도록 함
    public Token loginOrSignUp(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}