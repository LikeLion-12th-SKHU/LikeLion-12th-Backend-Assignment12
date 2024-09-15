package com.likelion.likelionoauthlogin.post.application;

import com.likelion.likelionoauthlogin.global.service.S3Service;
import com.likelion.likelionoauthlogin.post.api.dto.request.PostSaveReqDto;
import com.likelion.likelionoauthlogin.post.api.dto.response.PostInfoResDto;
import com.likelion.likelionoauthlogin.post.domain.Post;
import com.likelion.likelionoauthlogin.post.domain.repository.PostRepository;
import com.likelion.likelionoauthlogin.user.domain.User;
import com.likelion.likelionoauthlogin.user.domain.repository.UserRepository;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public Post savePost(PostSaveReqDto postSaveReqDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Post post = Post.builder()
                .title(postSaveReqDto.title())
                .content(postSaveReqDto.content())
                .user(user)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public PostInfoResDto postSave(PostSaveReqDto postSaveReqDto, MultipartFile multipartFile,
                                   Principal principal) throws IOException {
        Long id = Long.parseLong(principal.getName());
        String imageUrl = s3Service.upload(multipartFile, "post");

        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 없습니다. id =" + id));

        Post post = Post.builder()
                .title(postSaveReqDto.title())
                .content(postSaveReqDto.content())
                .imageUrl(imageUrl)
                .user(user)
                .build();

        postRepository.save(post);

        return PostInfoResDto.from(post);
    }

}
