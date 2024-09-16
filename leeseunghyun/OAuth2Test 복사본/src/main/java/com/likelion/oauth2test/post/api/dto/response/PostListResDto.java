package com.likelion.oauth2test.post.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PostListResDto(
        List<PostInfoResDto> posts // PostInfoResDto로 수정
) {
    public static PostListResDto from(List<PostInfoResDto> posts) { // List<PostInfoResDto>로 수정
        return PostListResDto.builder()
                .posts(posts)
                .build();
    }
}