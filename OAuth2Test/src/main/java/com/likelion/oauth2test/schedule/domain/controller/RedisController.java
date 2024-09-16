package com.likelion.oauth2test.schedule.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.oauth2test.global.exception.model.BaseResponse;
import com.likelion.oauth2test.global.exception.model.Success;
import com.likelion.oauth2test.schedule.domain.controller.request.RedisDto;
import com.likelion.oauth2test.schedule.domain.service.RedisService;

/**
 * Redis 동작 Controller
 *
 * @author : soohyun
 * @fileName : RedisController
 * @since : 8/18/24
 */
@RestController
@RequestMapping("/api/v1/redis")
public class RedisController {

	private final RedisService redisService;

	public RedisController(RedisService redisService) {
		this.redisService = redisService;
	}

	/**
	 * Redis의 값을 조회합니다.
	 *
	 * @param redisDto
	 * @return
	 */
	@GetMapping("/getValue")
	public ResponseEntity<BaseResponse<Object>> getValue(@RequestBody RedisDto redisDto) {
		String result = redisService.getValue(redisDto.key());
		return new ResponseEntity<>(BaseResponse.success(Success.GET_SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * Redis의 값을 추가/수정합니다.
	 *
	 * @param redisDto
	 * @return
	 */
	@PostMapping("/setValue")
	public ResponseEntity<BaseResponse<Object>> setValue(@RequestBody RedisDto redisDto) {
		int result = 0;

		if (redisDto.duration() == null) {
			result = redisService.setValues(redisDto.key(), redisDto.value());
		} else {
			result = redisService.setValues(redisDto.key(), redisDto.value(), redisDto.duration());
		}
		return new ResponseEntity<>(BaseResponse.success(Success.POST_UPDATE_SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * Redis의 key 값을 기반으로 row를 제거합니다.
	 *
	 * @param redisDto
	 * @return
	 */
	@PostMapping("/deleteValue")
	public ResponseEntity<BaseResponse<Object>> deleteRow(@RequestBody RedisDto redisDto) {
		int result = redisService.deleteValue(redisDto.key());
		return new ResponseEntity<>(BaseResponse.success(Success.GET_SUCCESS, result), HttpStatus.OK);

	}
}

