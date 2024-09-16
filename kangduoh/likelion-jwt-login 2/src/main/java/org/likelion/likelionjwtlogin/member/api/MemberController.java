package org.likelion.likelionjwtlogin.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.likelion.likelionjwtlogin.global.template.RspTemplate;
import org.likelion.likelionjwtlogin.member.api.dto.request.MemberLoginReqDto;
import org.likelion.likelionjwtlogin.member.api.dto.request.MemberSaveReqDto;
import org.likelion.likelionjwtlogin.member.api.dto.response.MemberLoginResDto;
import org.likelion.likelionjwtlogin.member.application.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "회원가입/로그인", description = "회원가입/로그인을 담당하는 api 그룹")
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 받아 회원가입 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public RspTemplate<String> join(@RequestBody @Valid MemberSaveReqDto memberSaveReqDto) {
        memberService.join(memberSaveReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "회원가입");
    }

    @GetMapping
    @Operation(
            summary = "로그인",
            description = "사용자의 이메일, 비밀번호를 받습니다. 암호화되어 저장된 비밀번호와 일치하는지 검사합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 암호 입력"),
                    @ApiResponse(responseCode = "404", description = "이메일을 찾을수 없거나, 잘못된 값을 입력"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public RspTemplate<MemberLoginResDto> login(@RequestBody @Valid MemberLoginReqDto memberLoginReqDto) {
        MemberLoginResDto memberLoginResDto = memberService.login(memberLoginReqDto);
        return new RspTemplate<>(HttpStatus.OK, "로그인", memberLoginResDto);
    }
}
