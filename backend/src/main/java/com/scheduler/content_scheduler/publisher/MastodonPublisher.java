package com.scheduler.content_scheduler.publisher;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.user.service.UserTokenService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MastodonPublisher implements PlatformPublisher {

    private static final String STATUSES_ENDPOINT = "/api/v1/statuses";

    private final OkHttpClient client;
    private final UserTokenService userTokenService;
    private final String mastodonBaseUrl;

    public MastodonPublisher(
            OkHttpClient client,
            UserTokenService userTokenService,
            @Value("${mastodon.base-url}") String mastodonBaseUrl
    ) {
        this.client = client;
        this.userTokenService = userTokenService;
        this.mastodonBaseUrl = mastodonBaseUrl;
    }

    @Override
    public Platform supports() {
        return Platform.MASTODON;
    }

    @Override
    public void publish(PlatformPost post) {

        String accessToken = userTokenService.getAccessToken(
                post.getCanonicalPost().getAuthor(),
                Platform.MASTODON
        );

        if (accessToken == null) {
            throw new IllegalStateException("No Mastodon access token available");
        }

        String jsonBody = """
                {
                  "status": "%s"
                }
                """.formatted(escape(post.getCanonicalPost().getContent()));

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(mastodonBaseUrl + STATUSES_ENDPOINT)
                .post(body)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(
                        "Mastodon publish failed: HTTP "
                                + response.code() + " - "
                                + response.body().string()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error publishing to Mastodon", e);
        }
    }

    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }
}

