package com.scheduler.content_scheduler.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TwitterConfiguration.class);

    private final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public Twitter twitter() {
        String apiKey = dotenv.get("TWITTER_API_KEY");
        String apiSecret = dotenv.get("TWITTER_API_SECRET");
        String accessToken = dotenv.get("TWITTER_ACCESS_TOKEN");
        String accessSecret = dotenv.get("TWITTER_ACCESS_SECRET");

        log.info("Initializing Twitter with API Key: {}", apiKey);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(apiKey)
                .setOAuthConsumerSecret(apiSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
