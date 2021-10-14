package com.sys.market.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

// 마지막 로그아웃 시간 저장. 저장된 시간보다 이전에 발급된 token 들은 유효하지 않은것으로 처리한다.
@Repository
@RequiredArgsConstructor
public class SignOutRepository {
    private final StringRedisTemplate redisTemplate;

    public static final String SIGN_OUT_PREFIX = "sign-out:";

    public void save(String userId, long time, long expireSeconds){
        String key = getKey(userId);
        String value = Long.toString(time/1000*1000);
        Duration timeout = Duration.ofSeconds(expireSeconds);

        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public Long find(String userId){
        String key = getKey(userId);
        String value = redisTemplate.opsForValue().get(key);

        return (value != null)? Long.parseLong(value) : null;
    }

    public void delete(String userId){
        redisTemplate.delete(getKey(userId));
    }

    private String getKey(String userId){
        return SIGN_OUT_PREFIX + userId;
    }
}
