package com.verygoodbank.tes.cache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@Component
public class DataLoader implements CommandLineRunner {
    private static final int PRODUCT_ID_INDEX = 0;
    private static final int PRODUCT_NAME_INDEX = 1;

    private final RedisTemplate<String, String> redisTemplate;

    public DataLoader(RedisTemplate<String, String> redisTemplate, RedisProperties redisProperties) throws IOException {
        this.redisTemplate = redisTemplate;
        RedisServer redisServer = new RedisServer(redisProperties.getRedisPort());
        redisServer.start();
    }

    @Override
    public void run(String... args) throws Exception {
        loadCsvDataIntoRedis();
    }

    private void loadCsvDataIntoRedis() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("product.csv"))))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    redisTemplate.opsForValue().set(parts[PRODUCT_ID_INDEX], parts[PRODUCT_NAME_INDEX]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
