package com.sys.market.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class VerificationCodeRepository {
    private final StringRedisTemplate redisTemplate;

    public static final String VERIFICATION_CODE_PREFIX = "code:";

    public void save(String code, String userId, long expireSeconds){
        String key = getKey(code);
        Duration timeout = Duration.ofSeconds(expireSeconds);
        redisTemplate.opsForValue().set(key, userId, timeout);
    }

    public String find(String code){
        return redisTemplate.opsForValue().get(getKey(code));
    }

    public void delete(String code){
        redisTemplate.delete(getKey(code));
    }

    private String getKey(String code){
        return VERIFICATION_CODE_PREFIX + code;
    }
}
