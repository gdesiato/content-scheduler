package com.scheduler.content_scheduler.integrations.twitter;

import com.scheduler.content_scheduler.integrations.oauth.OAuthProvider;
import com.scheduler.content_scheduler.post.model.Platform;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class TwitterOAuthProvider implements OAuthProvider {

    private final TwitterOAuthConfiguration config;

    public TwitterOAuthProvider(TwitterOAuthConfiguration config) {
        this.config = config;
    }

    @Override
    public Platform getPlatform() {
        return Platform.TWITTER;
    }

    @Override
    public boolean usesPkce() {
        return true;
    }

    @Override
    public String buildAuthorizationUrl(String state, String codeChallenge) {
        return UriComponentsBuilder
                .fromHttpUrl("https://twitter.com/i/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("scope", "tweet.read tweet.write users.read offline.access")
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .toUriString();
    }

    @Override
    public String getTokenEndpoint() {
        return "https://api.twitter.com/2/oauth2/token";
    }

    @Override
    public Map<String, String> buildTokenRequestParams(
            String code,
            String codeVerifier
    ) {
        return Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", config.getRedirectUri(),
                "client_id", config.getClientId(),
                "code_verifier", codeVerifier
        );
    }
}
