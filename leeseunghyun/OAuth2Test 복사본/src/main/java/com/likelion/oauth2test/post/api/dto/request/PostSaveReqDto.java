package com.likelion.oauth2test.post.api.dto.request;

import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.user.domain.Users;
import jakarta.validation.constraints.NotBlank;
public record PostSaveReqDto(
        @NotBlank(message = "게시글 제목은 필수로 입력해야 합니다.")
        String title,

        @NotBlank(message = "게시글 내용은 필수로 입력해야 합니다.")
        String content
) {

    // DTO를 Post 엔티티로 변환하는 메서드
    public Post toEntity(Users user, String image) {
        return Post.builder()
                .title(title)      // 제목 설정
                .content(content)  // 내용 설정
                .image(image)      // 이미지 URL 설정
                .user(user)        // 작성자 설정
                .build();
}
}
