package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.configuration.TwitterOAuthConfiguration;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@RestController
public class TwitterAuthController {

    private final static Logger log = LoggerFactory.getLogger(TwitterAuthController.class);

    private final TwitterOAuthConfiguration config;

    public TwitterAuthController(TwitterOAuthConfiguration config) {
        this.config = config;
    }

    @GetMapping("/authorize")
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        String codeVerifier = "your_code_verifier"; // Generate securely and store for later
        String codeChallenge = generateCodeChallenge(codeVerifier);

        return "https://twitter.com/i/oauth2/authorize"
                + "?response_type=code"
                + "&client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&scope=tweet.read tweet.write users.read offline.access"
                + "&state=" + state
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256";
    }

    @PostMapping("/callback")
    public String handleCallback(@RequestParam String code) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", config.getRedirectUri())
                .add("client_id", config.getClientId())
                .add("code_verifier", "your_code_verifier") // Same as used in /authorize
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
            log.info("Access token response: {}", responseBody);
            return responseBody; // Return or store the token
        } catch (IOException e) {
            throw new RuntimeException("Error exchanging code for access token", e);
        }
    }


    private String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating code challenge", e);
        }
    }
}
