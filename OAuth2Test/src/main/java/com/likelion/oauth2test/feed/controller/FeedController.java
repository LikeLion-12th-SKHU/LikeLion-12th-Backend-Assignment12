package com.likelion.oauth2test.feed.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.oauth2test.feed.controller.dto.request.UploadImageRequestDto;
import com.likelion.oauth2test.feed.controller.dto.response.FeedListResponse;
import com.likelion.oauth2test.feed.controller.dto.response.FeedResponse;
import com.likelion.oauth2test.feed.service.FeedService;
import com.likelion.oauth2test.global.exception.model.BaseResponse;
import com.likelion.oauth2test.global.exception.model.Success;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feed")
public class FeedController {
	private final FeedService feedService;

	public FeedController(FeedService feedService) {
		this.feedService = feedService;
	}

	@ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 각각 이미지의 url list를 json 형식으로 반환합니다.")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<FeedResponse> uploadImage(@Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 multipartFile 입니다.") UploadImageRequestDto uploadImageRequestDto, @AuthenticationPrincipal String userId){
		return BaseResponse.success(Success.POST_SAVE_SUCCESS,feedService.uploadImage(uploadImageRequestDto, userId));
	}

	@GetMapping
	public BaseResponse<FeedListResponse> getAllFeedsOfUser(@AuthenticationPrincipal String userId){
		return BaseResponse.success(Success.GET_SUCCESS, feedService.getAllFeedsOfUser(userId));
	}
}
