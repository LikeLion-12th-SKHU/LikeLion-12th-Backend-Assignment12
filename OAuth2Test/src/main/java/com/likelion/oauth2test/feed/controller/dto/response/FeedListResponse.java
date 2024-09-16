package com.likelion.oauth2test.feed.controller.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record FeedListResponse(List<FeedResponse> imageList) {
	public static FeedListResponse from(List<FeedResponse> imageList){
		return FeedListResponse.builder()
			.imageList(imageList)
			.build();
	}
}
