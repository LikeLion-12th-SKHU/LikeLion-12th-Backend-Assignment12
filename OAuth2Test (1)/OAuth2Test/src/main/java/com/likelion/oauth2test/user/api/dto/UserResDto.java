package com.likelion.oauth2test.user.api.dto;

import com.likelion.oauth2test.user.domain.User;
import lombok.Builder;

@Builder
public record UserResDto(
        Long id,
        String name,
        String email,
        String pictureUrl
) {
    public static UserResDto from(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .pictureUrl(user.getPictureUrl())
                .build();

    }
}
