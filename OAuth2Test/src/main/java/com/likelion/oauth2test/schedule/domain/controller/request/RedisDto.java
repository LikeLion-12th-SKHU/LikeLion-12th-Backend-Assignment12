package com.likelion.oauth2test.schedule.domain.controller.request;

import java.time.Duration;

public record RedisDto(String key, String value, Duration duration) {
}
