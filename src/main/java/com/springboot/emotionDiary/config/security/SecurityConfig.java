package com.springboot.emotionDiary.config.security;

import com.springboot.emotionDiary.config.jwt.JwtAuthenticationFilter;
import com.springboot.emotionDiary.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    // filterChain() : HttpSecurity를 구성하여 보안 설정 정의
    // "members/sign-in" 경로에 대한 요청은 모든 사용자에게 허용
    // "content/addArticle" 경로에 대한 요청은 USER 권한을 가진 사용자만 허용
    // 나머지 모든 요청은 인증을 필요로 함
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable) // *
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/", "/members/sign-in").permitAll() // 해당 API에 대해서는 모든 요청을 허가
                        .requestMatchers("/content/role/newArticle").hasRole("USER") // USER 권한이 있어야 요청할 수 있음
                        .anyRequest().permitAll()) // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정

                // JWT 인증을 위해 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    // DelegatingPasswordEncoder 생성하여 반환
    // DelegatingPasswordEncoder는 여러 암호화 알고리즘을 지원하며,
    // Spring Security의 기본 권장 알고리즘을 사용하여 비밀번호를 인코딩
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
