package com.scheduler.content_scheduler.configuration;

import java.util.Map;

public interface OAuthProvider {

    String getPlatform();
    boolean usesPkce();

    String buildAuthorizationUrl(String state, String codeChallenge);

    String getTokenEndpoint();

    Map<String, String> buildTokenRequestParams(
            String code,
            String codeVerifier
    );
}
