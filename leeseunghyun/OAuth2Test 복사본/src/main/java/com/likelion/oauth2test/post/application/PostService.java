package com.likelion.oauth2test.post.application;

import com.likelion.oauth2test.post.api.dto.request.PostSaveReqDto;
import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.post.api.dto.response.PostInfoResDto;
import com.likelion.oauth2test.post.api.dto.response.PostListResDto;
import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.post.domain.repository.PostRepository;
//import com.likelion.oauth2test.s3.S3Service;
import com.likelion.oauth2test.service.S3Service;
import com.likelion.oauth2test.user.domain.Users;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;

    // 게시글 저장
    @Transactional
    public PostInfoResDto savePost(PostSaveReqDto postSaveRequest, MultipartFile image, Principal principal) throws IOException {
        String imageUrl = s3Service.upload(image, "post"); // 이미지 업로드
        Long userId = Long.parseLong(principal.getName()); // 사용자 ID

        // 사용자 조회
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + userId));

        // 게시글 엔티티 생성 및 저장
        Post post = postSaveRequest.toEntity(user, imageUrl);
        postRepository.save(post);

        return PostInfoResDto.from(post); // 응답 DTO로 변환 후 반환
    }

    // 사용자 ID로 게시글 전체 조회
    public PostListResDto getAllPosts(Principal principal) {
        Long userId = Long.parseLong(principal.getName()); // 사용자 ID
        List<Post> posts = postRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 게시글을 찾을 수 없습니다. id = " + userId));

        List<PostInfoResDto> postResponses = posts.stream()
                .map(PostInfoResDto::from)
                .collect(Collectors.toList());

        return PostListResDto.from(postResponses); // 응답 DTO로 변환 후 반환
    }

    // 특정 게시글 조회
    public PostInfoResDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + postId));

        return PostInfoResDto.from(post); // 응답 DTO로 변환 후 반환
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + postId));
        postRepository.delete(post);
    }

    // 게시글 수정
    @Transactional
    public PostInfoResDto updatePost(Long postId, PostUpdateReqDto postUpdateRequest, MultipartFile image, Principal principal) throws IOException {
        Long userId = Long.parseLong(principal.getName()); // 사용자 ID

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + postId));

        // 게시글 업데이트
        post.update(postUpdateRequest);

        // 이미지가 있는 경우 S3에 업로드하고 URL 업데이트
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.upload(image, "post");
            post.updateImage(imageUrl);
        }

        postRepository.save(post);
        return PostInfoResDto.from(post); // 응답 DTO로 변환 후 반환
    }
}
