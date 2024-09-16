package com.likelion.oauth2test.post.api;

import com.likelion.oauth2test.post.api.dto.request.PostRequestDto;
import com.likelion.oauth2test.post.api.dto.response.PostResponseDto;
import com.likelion.oauth2test.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글을 작성합니다.",
            description = "사용자가 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자가 게시글을 작성하였습니다.."),
            @ApiResponse(responseCode = "401", description = "인증이 필요한 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류! 관리자에게 문의 바랍니다.")
    })

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//멀티파트 폼 데이터를 사용한다
    public ResponseEntity<PostResponseDto> postSave
            (PostRequestDto postRequestDto, @RequestPart("image") MultipartFile image, Principal principal) throws IOException {
        PostResponseDto postResponseDto = postService.postSave(postRequestDto, image, principal);
        //principal: 인증된 사용자의 정보를 포함하는 Principal 객체
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }
}