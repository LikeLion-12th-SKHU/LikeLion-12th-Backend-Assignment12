package com.likelion.oauth2test.global.controller;

import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.post.api.dto.response.PostResponseDto;
import com.likelion.oauth2test.service.AuthLoginService;
import com.likelion.oauth2test.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;
    private final PostService postService;

    @Operation(summary = "로그인한 사용자가 쓴 게시글 조회",
            description = "로그인한 사용자가 자기가 쓴 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하셨습니다!"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/user")
    public ResponseEntity<PostResponseDto> userPostFind(Principal principal) {
        PostResponseDto postResponseDto = postService.userPostFind(principal);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "구글 OAuth2 인증 콜백",
            description = "구글 OAuth2 인증 콜백을 통해 액세스 토큰을 얻고 로그인 또는 회원가입을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 또는 회원가입에 성공하셨습니다!"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/code/google")
    public Token googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    @Operation(summary = "로그인 또는 회원가입",
            description = "구글 액세스 토큰을 이용해 로그인 또는 회원가입을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 또는 회원가입에 성공하셨습니다!"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의 바랍니다.")
    })
    public Token loginOrSignup(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}
