package org.likelion.likelionjwtlogin.post.api.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@Tag(name = "게시물", description = "게시물을 담당하는 api 그룹")
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    @Operation(
            summary = "게시물 생성",
            description = "로그인한 사용자가 게시물을 작성합시다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시물 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public RspTemplate<String> postSave(@AuthenticationPrincipal String email,
                                        @RequestBody @Valid PostSaveReqDto postSaveReqDto) {
        postService.postSave(email, postSaveReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "게시물 생성");
    }

    @GetMapping()
    @Operation(
            summary = "내 게시물 조회",
            description = "로그인한 사용자가 작성한 게시물 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 게시물 리스트 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public ResponseEntity<PostListResDto> myPostFindAll(@AuthenticationPrincipal String email) {
        PostListResDto postListResDto = postService.postFindMember(email);
        return new ResponseEntity<>(postListResDto, HttpStatus.OK);
    }

}
