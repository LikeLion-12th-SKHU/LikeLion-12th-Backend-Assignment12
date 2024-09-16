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

    @GetMapping("/code/google")
    public Token googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    public Token loginOrSignup(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}