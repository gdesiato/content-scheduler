package com.scheduler.content_scheduler.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterOAuthConfiguration {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public TwitterOAuthConfiguration() {
        Dotenv dotenv = Dotenv.configure().load();
        this.clientId = dotenv.get("TWITTER_CLIENT_ID");
        this.clientSecret = dotenv.get("TWITTER_CLIENT_SECRET");
        this.redirectUri = dotenv.get("TWITTER_REDIRECT_URI");

        if (clientId == null || clientSecret == null || redirectUri == null) {
            throw new IllegalArgumentException("Twitter OAuth environment variables are not properly configured in the .env file.");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
