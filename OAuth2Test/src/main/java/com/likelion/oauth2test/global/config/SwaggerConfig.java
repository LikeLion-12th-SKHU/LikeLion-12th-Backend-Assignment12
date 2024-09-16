package com.likelion.oauth2test.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	private final String AUTH_TOKEN_HEADER = "Authorization";
	@Bean
	public OpenAPI openAPI(){
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo())
			.addSecurityItem(new SecurityRequirement().addList(AUTH_TOKEN_HEADER))
			.components(new Components()
				.addSecuritySchemes(AUTH_TOKEN_HEADER, new SecurityScheme()
					.name(AUTH_TOKEN_HEADER)
					.type(SecurityScheme.Type.HTTP)
					.scheme("Bearer")
					.bearerFormat("JWT")
				)
			);
	}
	private Info apiInfo(){
		return new Info()
			.title("Swagger 테스트")
			.description("Swagger 설명")
			.version("1.0.0");
	}
}

