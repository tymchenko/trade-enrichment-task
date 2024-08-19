package com.verygoodbank.tes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.embedded.RedisServer;

import java.io.IOException;

@SpringBootApplication
public class TradeEnrichmentServiceApplication {

	public static void main(String[] args) throws IOException {
		RedisServer redisServer = new RedisServer(6370);
		redisServer.start();

		SpringApplication.run(TradeEnrichmentServiceApplication.class, args);
	}

}
