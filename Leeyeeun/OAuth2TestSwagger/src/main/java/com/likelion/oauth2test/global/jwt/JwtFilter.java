package com.likelion.oauth2test.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HTTPServletRequest에서 JWT 토큰 추출
        String jwt = tokenProvider.resolveToken((HttpServletRequest) request);

        // JWT가 존재하고 유효한 경우
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // JWT로부터 Authentication 객체 추출
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            // SecurityContext에 Authentication 객체를 설정하여 인증 정보를 Spring Security에게 전달
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        // 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }
}