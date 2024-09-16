package com.likelion.oauth2test.user.api;

import com.likelion.oauth2test.service.UserService;
import com.likelion.oauth2test.user.api.dto.UserResponseDto;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 정보 조회",
            description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보를 성공적으로 조회했습니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요한 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의 바랍니다.")
    })
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserInfo(Principal principal) {
        return new ResponseEntity<>(userService.getUserInfo(principal), HttpStatus.OK);
    }

}
