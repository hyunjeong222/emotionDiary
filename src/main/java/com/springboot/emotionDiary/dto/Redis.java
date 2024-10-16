package com.springboot.emotionDiary.dto;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "MemberToken", timeToLive = 3600 * 24 * 14)
@Getter
@Setter
@AllArgsConstructor
public class Redis {
    @Id
    private String userId;
    private String refreshToken;
}
