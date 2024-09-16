package com.likelion.oauth2test.post.application;

import com.likelion.oauth2test.post.api.dto.request.PostSaveReqDto;
import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.post.api.dto.response.PostInfoResDto;
import com.likelion.oauth2test.post.api.dto.response.PostListResDto;
import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.post.domain.repository.PostRepository;
import com.likelion.oauth2test.s3.S3Service;
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
    public PostInfoResDto postSave(PostSaveReqDto postSaveReqDto, MultipartFile multipartFile, Principal principal) throws IOException {
        String image = s3Service.upload(multipartFile, "post");
        Long id = Long.parseLong(principal.getName());

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));

        Post post = postSaveReqDto.toEntity(user, image);
        postRepository.save(post);

        return PostInfoResDto.from(post);
    }

    // 사용자 id로 사용자가 쓴 게시글 조회
    @Transactional
    public PostListResDto userPostFind(Principal principal) {
        Long id = Long.parseLong(principal.getName());
        List<Post> posts = postRepository.findByUserId(id)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자의 게시글을 찾을 수 없습니다. id = " + id));

        List<PostInfoResDto> postInfoResDtoList = posts.stream()
                .map(PostInfoResDto::from)
                .toList();
        return PostListResDto.from(postInfoResDtoList);
    }

    // postid로 게시글 한 개 조회
    @Transactional
    public PostInfoResDto postFindOne(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new IllegalArgumentException("해당하는 게시글이 없습니다. id = " + postId));

        return PostInfoResDto.from(post);
    }

    // 모든 사용자의 게시글 전체 조회
    @Transactional
    public PostListResDto postFindAll() {
        List<Post> posts = postRepository.findAll();

        List<PostInfoResDto> postInfoResDtoList = posts.stream()
                .map(PostInfoResDto::from)
                .toList();
        return PostListResDto.from(postInfoResDtoList);
    }

    @Transactional
    public void deletePost(Long postId, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new IllegalArgumentException("해당하는 게시글이 없습니다. id =" + postId));

        Long id = post.getUser().getId();
        Long LoginId = Long.parseLong(principal.getName());

        if (!id.equals(LoginId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    @Transactional
    public PostInfoResDto updatePost(Long postId, PostUpdateReqDto postUpdateReqDto, MultipartFile image, Principal principal) throws IOException{
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new IllegalArgumentException("해당하는 게시글이 없습니다. id = " + postId));

        Long id = post.getUser().getId();
        Long LoginId = Long.parseLong(principal.getName());

        if (!id.equals(LoginId)) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        post.update(postUpdateReqDto);

        // 이미지가 있을 경우 S3에 업로드하고 URL 업데이트
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.upload(image, "post");
            post.updateImage(imageUrl);
        }
        postRepository.save(post);
        return PostInfoResDto.from(post);   // update 성공시 바로 수정한 정보 응답 리턴
    }
}
