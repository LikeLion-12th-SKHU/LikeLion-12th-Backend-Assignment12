package com.likelion.oauth2test.post.api.dto.response;

import com.likelion.oauth2test.post.domain.Post;
import lombok.Builder;

@Builder
public record PostResponseDto(
        Long id,
        String title,
        String imgUrl,
        String content
) {
    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imgUrl(post.getImgUrl())
                .content(post.getContent())
                .build();
    }
}