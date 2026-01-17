package com.scheduler.content_scheduler.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

    private final UserService userService;

    public UserTokenService(UserService userService) {
        this.userService = userService;
    }

    public void persistTokensForUser(
            UserEntity user,
            Platform platform,
            JsonNode token
    ) {
        // access_token (required)
        JsonNode accessTokenNode = token.get("access_token");
        if (accessTokenNode == null || accessTokenNode.isNull()) {
            throw new IllegalStateException("OAuth response missing access_token");
        }
        String accessToken = accessTokenNode.asText();

        // refresh_token (optional)
        String refreshToken = null;
        JsonNode refreshTokenNode = token.get("refresh_token");
        if (refreshTokenNode != null && !refreshTokenNode.isNull()) {
            refreshToken = refreshTokenNode.asText();
        }

        userService.savePlatformToken(
                user,
                platform,
                accessToken,
                refreshToken
        );
    }

    public String getAccessToken(UserEntity user, Platform platform) {
        return userService
                .getPlatformToken(user, platform)
                .getAccessToken();
    }

    public String getInstanceUrl(UserEntity user, Platform platform) {
        return userService
                .getPlatformToken(user, platform)
                .getInstanceUrl();
    }
}
