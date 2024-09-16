package com.likelion.oauth2test.post.api.dto.response;

import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.user.domain.User;
import lombok.Builder;

@Builder
public record PostInfoResDto(
        Long id,
        String title,
        String image,
        String content
) {
    public static PostInfoResDto from(Post post) {
        return PostInfoResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .image(post.getImage())
                .content(post.getContent())
                .build();
    }
}
