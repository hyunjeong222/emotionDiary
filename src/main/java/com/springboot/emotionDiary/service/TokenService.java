package com.springboot.emotionDiary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
