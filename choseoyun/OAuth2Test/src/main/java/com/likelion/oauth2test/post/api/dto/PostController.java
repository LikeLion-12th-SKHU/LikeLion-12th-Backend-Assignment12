package com.likelion.oauth2test.post.api.dto;

import com.likelion.oauth2test.post.api.dto.request.PostSaveReqDto;
import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.post.api.dto.response.PostInfoResDto;
import com.likelion.oauth2test.post.api.dto.response.PostListResDto;
import com.likelion.oauth2test.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/user")    // 로그인한 사용자가 쓴 게시글 조회
    public ResponseEntity<PostListResDto> userPostFind(Principal principal) {
        PostListResDto postListResDto = postService.userPostFind(principal);
        return new ResponseEntity<>(postListResDto, HttpStatus.OK);
    }

    @GetMapping("/{postId}")    // postid로 게시글 한 개 조회
    public ResponseEntity<PostInfoResDto> postFindOne(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.postFindOne(postId), HttpStatus.OK);
    }

    @GetMapping // 모든 게시글 조회
    public ResponseEntity<PostListResDto> postFindAll() {
        PostListResDto postListResDto = postService.postFindAll();
        return new ResponseEntity<>(postListResDto, HttpStatus.OK);
    }

    @Operation(summary = "로그인한 사용자가 쓴 게시글 조회",
            description = "로그인한 사용자가 자기가 쓴 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자 문의.")
    })

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostInfoResDto> postSave(@RequestPart("post") PostSaveReqDto postSaveReqDto,
                                                   @RequestPart("image") MultipartFile image,
                                                   Principal principal) throws IOException {
        PostInfoResDto postInfoResDto = postService.postSave(postSaveReqDto, image, principal);
        return new ResponseEntity<>(postInfoResDto, HttpStatus.CREATED);

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId, Principal principal) {
        postService.deletePost(postId, principal);
        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
    }

    @PatchMapping(value = "/{postId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostInfoResDto> updatePost(@PathVariable("postId") Long postId,
                                                     @RequestPart("post") PostUpdateReqDto postUpdateReqDto,
                                                     @RequestPart("image") MultipartFile image,
                                                     Principal principal) throws IOException{
        PostInfoResDto postInfoResDto = postService.updatePost(postId, postUpdateReqDto, image, principal);
        return new ResponseEntity<>(postInfoResDto, HttpStatus.OK);
    }

}
