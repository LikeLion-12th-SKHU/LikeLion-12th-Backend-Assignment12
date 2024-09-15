package com.likelion.likelionoauthlogin.post.api.dto.response;

import com.likelion.likelionoauthlogin.post.domain.Post;
import lombok.Builder;

@Builder
public record PostInfoResDto(
        Long id,
        String title,
        String imageUrl,
        String content
) {
    public static PostInfoResDto from(Post post) {
        return PostInfoResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .content(post.getContent())
                .build();
    }
}
