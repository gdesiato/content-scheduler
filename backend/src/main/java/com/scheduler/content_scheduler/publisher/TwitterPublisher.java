package com.scheduler.content_scheduler.publisher;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PlatformPost;
import org.springframework.beans.factory.annotation.Value;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TwitterPublisher implements PlatformPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(TwitterPublisher.class);

    private final OkHttpClient client = new OkHttpClient();

    @Value("${twitter.bearer-token}")
    private String bearerToken;

    @Override
    public Platform supports() {
        return Platform.TWITTER;
    }

    @Override
    public void publish(PlatformPost post) {
        MediaType mediaType = MediaType.parse("application/json");
        String tweetContent = "{\"text\":\"" + post.getContent() + "\"}";
        RequestBody body = RequestBody.create(tweetContent, mediaType);

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets")
                .post(body)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(
                        "Failed to post tweet: " + response.body().string()
                );
            }
            log.info("Tweet posted successfully");
        } catch (IOException e) {
            throw new RuntimeException("Error posting tweet", e);
        }
    }
}
