package com.scheduler.content_scheduler.ingestion.mastodon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.content_scheduler.ingestion.SourceAdapter;
import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.source.mastodon.dto.MastodonStatusDto;
import com.scheduler.content_scheduler.user.service.UserTokenService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class MastodonSourceAdapter implements SourceAdapter {

    private static final String STATUSES_ENDPOINT = "/api/v1/accounts/verify_credentials";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final UserTokenService userTokenService;

    public MastodonSourceAdapter(
            OkHttpClient httpClient,
            ObjectMapper objectMapper,
            UserTokenService userTokenService
    ) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.userTokenService = userTokenService;
    }

    @Override
    public Platform platform() {
        return Platform.MASTODON;
    }

    @Override
    public List<CanonicalPost> fetchNewPosts(UserEntity user) {

        String accessToken = userTokenService
                .getAccessToken(user, Platform.MASTODON);

        String instanceBaseUrl = userTokenService
                .getInstanceUrl(user, Platform.MASTODON); // e.g. https://mastodon.social

        String url = instanceBaseUrl + "/api/v1/accounts/verify_credentials";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IllegalStateException("Failed to fetch Mastodon account info");
            }

            String accountJson = response.body().string();
            String accountId = objectMapper.readTree(accountJson).get("id").asText();

            return fetchStatuses(user, accountId, accessToken, instanceBaseUrl);

        } catch (Exception e) {
            throw new RuntimeException("Mastodon ingestion failed", e);
        }
    }

    private List<CanonicalPost> fetchStatuses(
            UserEntity user,
            String accountId,
            String accessToken,
            String baseUrl
    ) throws Exception {

        String url = baseUrl + "/api/v1/accounts/" + accountId + "/statuses";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IllegalStateException("Failed to fetch Mastodon statuses");
            }

            MastodonStatusDto[] statuses =
                    objectMapper.readValue(response.body().string(), MastodonStatusDto[].class);

            List<CanonicalPost> result = new ArrayList<>();

            for (MastodonStatusDto status : statuses) {

                CanonicalPost post = new CanonicalPost(
                        Platform.MASTODON,
                        status.id,
                        user.getId(),
                        stripHtml(status.content),
                        Instant.parse(status.created_at)
                );

                result.add(post);
            }

            return result;
        }
    }

    /**
     * Mastodon content is HTML — we normalize to plain text for canonical storage.
     */
    private String stripHtml(String html) {
        return html
                .replaceAll("<[^>]*>", "")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }
}
