package com.scheduler.content_scheduler.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TwitterConfiguration.class);
    private final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public String bearerToken() {
        String bearerToken = dotenv.get("TWITTER_BEARER_TOKEN");
        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new IllegalArgumentException("TWITTER_BEARER_TOKEN is not set in .env");
        }
        log.info("Bearer Token loaded successfully.");
        return bearerToken;
    }
}

