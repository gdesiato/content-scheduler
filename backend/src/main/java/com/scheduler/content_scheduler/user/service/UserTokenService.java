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

    public void persistTokens(Platform platform, JsonNode token) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("User not authenticated");
        }

        JsonNode accessTokenNode = token.get("access_token");
        if (accessTokenNode == null || accessTokenNode.isNull()) {
            throw new IllegalStateException("OAuth response missing access_token");
        }

        JsonNode refreshTokenNode = token.get("refresh_token");

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        userService.savePlatformToken(
                user,
                platform,
                accessTokenNode.asText(),
                refreshTokenNode != null && !refreshTokenNode.isNull()
                        ? refreshTokenNode.asText()
                        : null
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

