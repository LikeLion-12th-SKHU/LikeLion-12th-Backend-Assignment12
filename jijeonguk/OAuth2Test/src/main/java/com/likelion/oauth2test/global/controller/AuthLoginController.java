package com.likelion.oauth2test.global.controller;

import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.service.AuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;

    //콘솔에 Authorization code 출력
//    @GetMapping("/code/{registrationId}")
//    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
//        authLoginService.socialLogin(code, registrationId);
//    }

    @Operation(summary = "OAuth2 구글 인증 콜백 입니다.",
            description = "OAuth2 구글 인증 콜백을 통해 토큰을 발급받고 로그인 또는 회원가입을 합니다..")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 또는 회원가입에 성공하셨습니다!"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류! 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/code/google") //인가 코드 받기
    public Token googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    public Token loginOrSignup(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}