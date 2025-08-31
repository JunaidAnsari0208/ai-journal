package com.junaid.ai_journal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitingService {
    private final StringRedisTemplate stringRedisTemplate;


    // Checks if a user is allowed to make a request based on maxRequests and windowSeconds.
    public boolean isAllowed(String key, int maxRequests, int windowSeconds) {
        // Atomically increment the key
        Long count = stringRedisTemplate.opsForValue().increment(key);

        // If this is the first request, set the expiry
        if (count == 1) {
            stringRedisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }

        // Allow request only if count <= maxRequests
        return count <= maxRequests;
    }
}
