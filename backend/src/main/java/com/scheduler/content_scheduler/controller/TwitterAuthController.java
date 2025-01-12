package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.configuration.TwitterOAuthConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TwitterAuthController {

    private final TwitterOAuthConfiguration config;

    public TwitterAuthController(TwitterOAuthConfiguration config) {
        this.config = config;
    }

    @GetMapping("/authorize")
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        String codeChallenge = "example_code_challenge";

        return "https://twitter.com/i/oauth2/authorize"
                + "?response_type=code"
                + "&client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&scope=tweet.read tweet.write users.read offline.access"
                + "&state=" + state
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=plain";
    }
}
