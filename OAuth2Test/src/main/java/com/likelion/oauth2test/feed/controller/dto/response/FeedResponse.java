package com.likelion.oauth2test.feed.controller.dto.response;

import com.likelion.oauth2test.feed.domain.Feed;

import lombok.Builder;

@Builder
public record FeedResponse(Long id, String title, String image, String content) {
	public static FeedResponse from(Feed feed){
		return FeedResponse.builder()
			.id(feed.getId())
			.title(feed.getTitle())
			.image(feed.getImage())
			.content(feed.getContent())
			.build();
	}
}
