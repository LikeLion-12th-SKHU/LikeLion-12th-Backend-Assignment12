package com.likelion.oauth2test.post.api.dto.response;

import com.likelion.oauth2test.post.domain.Post;
import lombok.Builder;

@Builder
public record PostResponseDto(
        Long id,
        String title,
        String image,
        String content
) {
    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .image(post.getImage())
                .content(post.getContent())
                .build();
    }
}