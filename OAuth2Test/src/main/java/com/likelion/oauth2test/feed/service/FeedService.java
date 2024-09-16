package com.likelion.oauth2test.feed.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.oauth2test.external.S3Service;
import com.likelion.oauth2test.feed.controller.dto.request.UploadImageRequestDto;
import com.likelion.oauth2test.feed.controller.dto.response.FeedListResponse;
import com.likelion.oauth2test.feed.controller.dto.response.FeedResponse;
import com.likelion.oauth2test.feed.domain.Feed;
import com.likelion.oauth2test.feed.domain.repository.FeedRepository;
import com.likelion.oauth2test.global.exception.NotFoundException;
import com.likelion.oauth2test.global.exception.model.Error;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {
	private final FeedRepository feedRepository;
	private final UserRepository userRepository;
	private final S3Service s3Service;

	@Transactional
	public FeedResponse uploadImage(UploadImageRequestDto uploadImageRequestDto, String userId) {
		User user = findUserInFeedService(userId);
		String imageUrl = s3Service.uploadImage(uploadImageRequestDto.feedImage(), "testimage");
		Feed feed = Feed.builder()
			.image(imageUrl)
			.content(uploadImageRequestDto.content())
			.title(uploadImageRequestDto.feedTitle())
			.user(user)
			.build();
		feedRepository.save(feed);
		return FeedResponse.from(feed);
	}

	public FeedListResponse getAllFeedsOfUser(String userId){
		User user = findUserInFeedService(userId);
		List<FeedResponse> feeds = feedRepository.findAllByUser(user)
			.stream()
			.map(FeedResponse::from)
			.collect(Collectors.toList());
		return FeedListResponse.from(feeds);
	}

	private User findUserInFeedService(String userId){
		return userRepository.findById(Long.parseLong(userId))
			.orElseThrow(
				() -> new NotFoundException(Error.MEMBERS_NOT_FOUND_EXCEPTION,
					Error.MEMBERS_NOT_FOUND_EXCEPTION.getMessage())
			);
	}

}
