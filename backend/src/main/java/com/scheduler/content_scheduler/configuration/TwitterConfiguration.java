package com.scheduler.content_scheduler.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfiguration {

    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.api.secret}")
    private String apiSecret;

    @Value("${twitter.bearer.token}")
    private String bearerToken;

    @Bean
    public Twitter twitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(apiKey)
                .setOAuthConsumerSecret(apiSecret)
                .setOAuth2TokenType("bearer")
                .setOAuth2AccessToken(bearerToken);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
