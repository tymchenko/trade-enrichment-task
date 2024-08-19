package com.verygoodbank.tes.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * This service can search product name in cache by product id
 */
@Slf4j
@Service
public class ProductNameCache {
    private final StringRedisTemplate redisTemplate;

    public ProductNameCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getProductDataById(String productId) {
        log.info("Searching for product name with product id: " + productId);
        return redisTemplate.opsForValue().get(productId);
    }
}
