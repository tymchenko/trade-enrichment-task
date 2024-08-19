package com.verygoodbank.tes.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductNameCache {
    private final StringRedisTemplate redisTemplate;

    public ProductNameCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getProductDataById(String productId) {
        return redisTemplate.opsForValue().get(productId);
    }
}
