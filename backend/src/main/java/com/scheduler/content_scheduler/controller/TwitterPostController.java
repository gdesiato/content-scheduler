package com.scheduler.content_scheduler.controller;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TwitterPostController {

    private final Dotenv dotenv = Dotenv.configure().load();

    @PostMapping("/post-tweet")
    public String postTweet(@RequestBody Map<String, String> tweetData) throws Exception {
        String accessToken = dotenv.get("TWITTER_ACCESS_TOKEN");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String tweetContent = "{\"text\":\"" + tweetData.get("text") + "\"}";

        okhttp3.RequestBody body = okhttp3.RequestBody.create(tweetContent, mediaType);

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets")
                .post(body)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) { // Try-with-resources
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to post tweet: " + response.body().string());
            }
            return response.body().string();
        }
    }
}
