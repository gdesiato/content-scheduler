package com.scheduler.content_scheduler.integrations.mastodon;

import com.scheduler.content_scheduler.integrations.oauth.OAuthProvider;
import com.scheduler.content_scheduler.post.model.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class MastodonOAuthProvider implements OAuthProvider {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String scopes;

    public MastodonOAuthProvider(
            @Value("${mastodon.client-id}") String clientId,
            @Value("${mastodon.client-secret}") String clientSecret,
            @Value("${mastodon.redirect-uri}") String redirectUri,
            @Value("${mastodon.scopes:read write}") String scopes
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    @Override
    public Platform getPlatform() {
        return Platform.MASTODON;
    }

    @Override
    public boolean usesPkce() {
        return true;
    }

    /**
     * Build the authorization URL for Mastodon
     */
    @Override
    public String buildAuthorizationUrl(String state, String codeChallenge) {
        // NOTE: instance URL must be resolved before calling this
        String instanceBaseUrl = getInstanceBaseUrl();

        return UriComponentsBuilder
                .fromHttpUrl(instanceBaseUrl + "/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scopes)
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .toUriString();
    }

    /**
     * Mastodon token endpoint
     */
    @Override
    public String getTokenEndpoint() {
        return getInstanceBaseUrl() + "/oauth/token";
    }

    /**
     * Build POST params for token exchange
     */
    @Override
    public Map<String, String> buildTokenRequestParams(
            String code,
            String codeVerifier
    ) {
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("scope", scopes);
        params.put("code_verifier", codeVerifier);

        return params;
    }

    private String getInstanceBaseUrl() {
        return "https://mastodon.social";
    }
}
