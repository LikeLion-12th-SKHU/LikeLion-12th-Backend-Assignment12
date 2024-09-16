package com.likelion.oauth2test.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String AUTH_TOKEN_HEADER = "Authorization";

    private Info apiInfo() {
        return new Info()
                .title("Swagger 테스트") // api의 제목
                .description("Swagger 설명") // api 설명
                .version("1.0.0"); // api 버전
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo()) // API 정보 설정
                .addSecurityItem(new SecurityRequirement().addList(AUTH_TOKEN_HEADER))
                // 보안 요구 사항 추가 - 토큰 헤더를 보안 요구사항으로 쓰겠다는 의미
                .components(new Components()
                        .addSecuritySchemes(AUTH_TOKEN_HEADER, new SecurityScheme()
                                // 보안 스키마 추가
                                .name(AUTH_TOKEN_HEADER) // 보안 스키마 이름
                                .type(SecurityScheme.Type.HTTP) // 스키마 타입
                                .scheme("Bearer") // 인증 스키마
                                .bearerFormat("JWT")));
    }
}
