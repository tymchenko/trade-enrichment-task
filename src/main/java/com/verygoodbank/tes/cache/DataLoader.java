package com.verygoodbank.tes.cache;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Objects;

/**
 * Loads products information to Redis on start up
 *
 */
@Component
@Slf4j
public class DataLoader {
    private static final int PRODUCT_ID_INDEX = 0;
    private static final int PRODUCT_NAME_INDEX = 1;
    private final String productNames;

    private final RedisTemplate<String, String> redisTemplate;

    public DataLoader(RedisTemplate<String, String> redisTemplate, @Value("${product.names}")String productNames) {
        this.redisTemplate = redisTemplate;
        this.productNames = productNames;
    }

    @PostConstruct
    void loadCsvDataIntoRedis() {
        log.info("Uploading product names to Redis...");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(productNames))));
             CSVReader csvReader = new CSVReader(reader)) {

            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] values = iterator.next();
                redisTemplate.opsForValue().set(values[PRODUCT_ID_INDEX], values[PRODUCT_NAME_INDEX]);
            }
        } catch (Exception e) {
            log.error("Exception during upload in Redis. " + e.getMessage());
        }
        log.info("Product names were uploaded to Redis.");
    }
}
