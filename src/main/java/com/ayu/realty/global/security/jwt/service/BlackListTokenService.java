package com.ayu.realty.global.security.jwt.service;

import com.ayu.realty.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public void blackListTokenSave(String accessToken) {

        // 1. Access Token에서 남은 만료 시간 계산
        Long expiration = jwtUtil.getExpiration(accessToken);

        // 2. Redis에 저장: key = "blacklist:{토큰}", value = "logout"
        redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

    }
}
