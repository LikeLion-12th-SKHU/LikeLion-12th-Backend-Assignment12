package com.likelion.oauth2test.post.api.dto.request;

import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.user.domain.User;
import jakarta.validation.constraints.NotBlank;

public record PostSaveReqDto(
        @NotBlank(message = "게시글 제목은 필수로 입력해야 합니다.")
        String title,

        @NotBlank(message = "게시글 내용은 필수로 입력해야 합니다.")
        String content

) {
        public Post toEntity(User user, String image) {
                return Post.builder()
                        .title(title)
                        .content(content)
                        .image(image)
                        .user(user)
                        .build();
        }
}
