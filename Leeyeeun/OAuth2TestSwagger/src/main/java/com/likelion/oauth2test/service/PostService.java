package com.likelion.oauth2test.service;

import com.likelion.oauth2test.post.api.dto.request.PostRequestDto;
import com.likelion.oauth2test.post.api.dto.response.PostResponseDto;
import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.post.repository.PostRepository;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Transactional
    public PostResponseDto postSave(PostRequestDto postRequestDto, MultipartFile multipartFile, Principal principal) throws IOException {
        String image = s3Service.upload(multipartFile, "post");
        Long id = Long.parseLong(principal.getName());

        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id)
        );

        Post post = postRequestDto.createPost(user, image);
        postRepository.save(post);

        return PostResponseDto.from(post);
    }

    // 사용자 id로 사용자가 쓴 게시글 조회
    @Transactional
    public PostResponseDto userPostFind(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        Post post = postRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 게시글을 찾을 수 없습니다. id = " + id))
                .get(0);

        return PostResponseDto.from(post);
    }


}