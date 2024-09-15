// 사용자의 요청을 받아 서비스 계층(PostService)에 처리를 위임하고 결과를 반환함

package org.likelion.likelionjwtlogin.post.api.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.likelion.likelionjwtlogin.global.template.RspTemplate;
import org.likelion.likelionjwtlogin.post.api.dto.request.PostSaveReqDto;
import org.likelion.likelionjwtlogin.post.api.dto.response.PostListResDto;
import org.likelion.likelionjwtlogin.post.application.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController                         // 컨트롤러를 JSON 으로 반환하는 컨트롤러로 만든다.
@RequestMapping("/posts")            // URL 을 매핑하는데 사용하는 어노테이션(/---/posts)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "게시글 생성",description = "인가를 받은 사용자가 게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 생성에 성공했다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의하세요.")
    })
    @PostMapping()                  // POST 요청을 받을 수 있게 하는 API(공란)
    public RspTemplate<String> postSave(@AuthenticationPrincipal String email,
                                        @RequestBody @Valid PostSaveReqDto postSaveReqDto) {
        postService.postSave(email, postSaveReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "게시물 생성");
    }

    @Operation(summary = "게시글 모두 찾기",description = "인가를 받은 사용자가 자신이 쓴 게시글을 모두 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의하세요.")
    })
    @GetMapping()                   // GET 요청을 받을 수 있게 하는 API(공란)
    public ResponseEntity<PostListResDto> myPostFindAll(@AuthenticationPrincipal String email) {
        PostListResDto postListResDto = postService.postFindMember(email);
        return new ResponseEntity<>(postListResDto, HttpStatus.OK);
    }

}

// @AuthenticationPrincipal :
// @RequestBody : 요청 메시지의 message body 를 자바 객체로 변환하여 준다.
// @Valid : 변환한 객체를 검증하는 자바 표준 함수. 예외 발생 시 MethodArgumentNotValidException 를 호출한다.
