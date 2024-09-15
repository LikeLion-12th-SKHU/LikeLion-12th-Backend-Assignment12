// Controller 와 Repository 를 잇는 역할을 한다.

package org.likelion.likelionjwtlogin.member.appication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.likelion.likelionjwtlogin.global.jwt.TokenProvider;
import org.likelion.likelionjwtlogin.member.api.dto.request.MemberLoginReqDto;
import org.likelion.likelionjwtlogin.member.api.dto.request.MemberSaveReqDto;
import org.likelion.likelionjwtlogin.member.api.dto.response.MemberLoginResDto;
import org.likelion.likelionjwtlogin.member.domain.Member;
import org.likelion.likelionjwtlogin.member.domain.Repository.MemberRepository;
import org.likelion.likelionjwtlogin.member.domain.Role;
import org.likelion.likelionjwtlogin.member.exception.InvalidMemberException;
import org.likelion.likelionjwtlogin.member.exception.NotFoundMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service                            // 서비스 인터페이스를 구현하는 클래스에서 사용
// 적용된 메서드는 실행 중 예외 발생 시 해당 메서드를 실행하면서 수행한 쿼리들을 모두 롤백, 정상인 경우 변경 사항을 저장한다. (읽기 전용)
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Operation(summary = "회원가입", description = "계정이 없는 사용자가 회원가입을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 생성에 성공했다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의하세요.")
    })
    // 회원가입
    @Transactional
    public void join(MemberSaveReqDto memberSaveReqDto, String email) {
        // 존재하는 이메일인지 검사
        if (memberRepository.existsByEmail(memberSaveReqDto.email())) {
            throw new InvalidMemberException("이미 존재하는 이메일입니다.");
        }
        Member member = Member.builder()
                .email(memberSaveReqDto.email())
                .pwd(passwordEncoder.encode(memberSaveReqDto.pwd()))
                .nickname(memberSaveReqDto.nickname())
                .accessToken(tokenProvider.generateToken(email))
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    @Operation(summary = "로그인", description = "계정이 있는 사용자가 인가를 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. / ID, PW 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의하세요.")
    })
    // 로그인
    public MemberLoginResDto login(MemberLoginReqDto memberLoginReqDto) {
        Member member = memberRepository.findByEmail(memberLoginReqDto.email())
                .orElseThrow(() -> new NotFoundMemberException());
        String token = tokenProvider.generateToken(member.getEmail());

        if (!passwordEncoder.matches(memberLoginReqDto.pwd(), member.getPwd())) {
            throw new InvalidMemberException("패스워드가 일치하지 않습니다.");
        }
        return MemberLoginResDto.of(member, token);
    }
}
