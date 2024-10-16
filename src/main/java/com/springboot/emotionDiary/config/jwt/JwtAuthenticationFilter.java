package com.springboot.emotionDiary.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    // redis
    private final RedisTemplate<String, Object> redisTemplate;

    // resolveToken() 메서드를 사용하여 요청 헤더에서 Jwt 토큰 추출
    // JwtTokenProvider validateToken() 메서드로 Jwt 토큰 유효성 검증
    // 토큰이 유효하면 JwtTokenProvider getAuthentication() 메서드로 인증 객체 가져와서 SecurityContext 저장
    // -> 요청을 처리하는 동안 인증 정보 유지
    // chain.doFilter() 호출하여 다음 필터로 요청 전달
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header에서 Jwt 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        // 2. validateToken으로 토큰 유효성 검사
        // redis
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // (추가) Redis에 해당 accessToken logout 여부 확인
            String isLogout = (String)redisTemplate.opsForValue().get(token);
            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    // HttpServletRequest(Request Header)에서 토큰 정보 추출
    // Authorization 헤더에서 "Bearer" 접두사로 시작하는 토큰을 추출하여 반환
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
