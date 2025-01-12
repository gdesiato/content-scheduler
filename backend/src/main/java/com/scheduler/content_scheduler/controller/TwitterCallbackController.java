package com.scheduler.content_scheduler.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.content_scheduler.configuration.TwitterOAuthConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

@RestController
public class TwitterCallbackController {

    private static final Logger log = LoggerFactory.getLogger(TwitterCallbackController.class);

    private final TwitterOAuthConfiguration config;

    public TwitterCallbackController(TwitterOAuthConfiguration config) {
        this.config = config;
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam String code) throws Exception {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", config.getRedirectUri())
                .add("client_id", config.getClientId())
                .add("code_verifier", "example_code_challenge")
                .build();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/oauth2/token")
                .post(body)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to exchange code: " + response.body().string());
            }

            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(responseBody);

            String accessToken = json.get("access_token").asText();
            String refreshToken = json.has("refresh_token") ? json.get("refresh_token").asText() : null;

            log.info("Access Token: {}", accessToken);
            log.info("Refresh Token: {}", refreshToken);

            return "Tokens received and stored!";
        }
    }
}

