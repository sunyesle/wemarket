package com.sys.market.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public static final String REFRESH_TOKEN_PREFIX = "refresh-token:";

    public void save(String userId, String refreshToken, long expireSeconds){
        String key = getKey(userId);
        Duration timeout = Duration.ofSeconds(expireSeconds);

        redisTemplate.opsForValue().set(key, refreshToken, timeout);
    }

    public String find(String userId){
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    public void delete(String userId){
        redisTemplate.delete(getKey(userId));
    }

    private String getKey(String userId){
        return REFRESH_TOKEN_PREFIX + userId;
    }
}
