package com.likelion.oauth2test.schedule.domain.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

/**
 * Redis Service를 관리하는 인터페이스
 *
 * @author : soohyun
 * @fileName : RedisService
 * @since : 8/18/24
 */
@Service
public interface RedisService {

	int setValues(String key, String value);                       // 값 등록 / 수정

	int setValues(String key, String value, Duration duration);    // 값 등록 / 수정

	String getValue(String key);                                    // 값 조회

	int deleteValue(String key);                                   // 값 삭제
}
