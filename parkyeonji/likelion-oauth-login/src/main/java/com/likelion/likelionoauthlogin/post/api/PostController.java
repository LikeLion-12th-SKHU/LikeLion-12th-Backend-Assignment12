package com.likelion.likelionoauthlogin.post.api;

import com.likelion.likelionoauthlogin.post.api.dto.request.PostSaveReqDto;
import com.likelion.likelionoauthlogin.post.api.dto.response.PostInfoResDto;
import com.likelion.likelionoauthlogin.post.application.PostService;
import com.likelion.likelionoauthlogin.post.domain.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api-docs/post")
public class PostController {
    private final PostService postService;

    @Operation(summary = "이미지 없이 게시글 생성",
            description = "이미지를 제외하고 게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자 문의")
    })
    @PostMapping("/{userId}")
    public ResponseEntity<Post> createPost(@PathVariable Long userId, @RequestBody PostSaveReqDto postSaveReqDto) {
        Post savedPost = postService.savePost(postSaveReqDto, userId);
        return ResponseEntity.ok(savedPost);
    }




    @Operation(summary = "이미지 포함하여 게시글 생성",
            description = "이미지를 포함하여 게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자 문의")
    })
    @PostMapping(value = "/imageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostInfoResDto> postSave(PostSaveReqDto postSaveReqDto,
                                           @RequestPart("image")MultipartFile image,
                                           Principal principal) throws IOException {
        PostInfoResDto postInfoResDto = postService.postSave(postSaveReqDto, image, principal);

        return new ResponseEntity<>(postInfoResDto, HttpStatus.CREATED);
    }


}
