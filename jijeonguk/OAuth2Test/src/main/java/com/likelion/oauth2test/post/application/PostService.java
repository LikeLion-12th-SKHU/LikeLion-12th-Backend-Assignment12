package com.likelion.oauth2test.post.application;

import com.likelion.oauth2test.post.api.dto.response.PostResponseDto;
import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.post.domain.repository.PostRepository;
import com.likelion.oauth2test.post.api.dto.request.PostRequestDto;
import com.likelion.oauth2test.post.api.dto.response.PostResponseDto;
import com.likelion.oauth2test.service.S3Service;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

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

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));

        Post post = postRequestDto.createPost(user, image);
        postRepository.save(post);

        return PostResponseDto.from(post);
    }
}