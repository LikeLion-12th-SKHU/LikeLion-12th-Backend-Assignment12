package com.likelion.oauth2test.post.api;

import com.likelion.oauth2test.post.api.dto.request.PostSaveReqDto;
import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.post.api.dto.response.PostListResDto;
import com.likelion.oauth2test.post.application.PostService;
import com.likelion.oauth2test.post.domain.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "로그인한 사용자가 쓴 게시글 조회", description = "로그인한 사용자가 쓴 게시글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자 문의")
    })
    @GetMapping("/user-posts")
    public ResponseEntity<?> userPostFind(Principal principal) {
        return ResponseEntity.ok(postService.getAllPosts(principal));
    }

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(Principal principal) {
        return ResponseEntity.ok(postService.getAllPosts(principal));
    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 게시글 저장
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePost(
            @RequestPart("post") PostSaveReqDto postSaveReqDto,
            @RequestPart("image") MultipartFile image,
            Principal principal) {
        try {
            return new ResponseEntity<>(postService.savePost(postSaveReqDto, image, principal), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("이미지 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 게시글 수정
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestPart("post") PostUpdateReqDto postUpdateReqDto,
            @RequestPart("image") MultipartFile image,
            Principal principal) {
        try {
            return ResponseEntity.ok(postService.updatePost(postId, postUpdateReqDto, image, principal));
        } catch (IOException e) {
            return new ResponseEntity<>("이미지 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}