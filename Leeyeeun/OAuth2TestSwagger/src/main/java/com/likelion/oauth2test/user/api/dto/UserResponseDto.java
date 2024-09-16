package com.likelion.oauth2test.user.api.dto;

import com.likelion.oauth2test.user.domain.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String name,
        String email,
        String pictureUrl
) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .pictureUrl(user.getPictureUrl())
                .build();

    }
}