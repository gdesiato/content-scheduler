package com.scheduler.content_scheduler.integrations.oauth;

import com.scheduler.content_scheduler.post.model.Platform;

import java.util.Map;

public interface OAuthProvider {

    Platform getPlatform();
    boolean usesPkce();

    String buildAuthorizationUrl(String state, String codeChallenge);

    String getTokenEndpoint();

    Map<String, String> buildTokenRequestParams(
            String code,
            String codeVerifier
    );
}
