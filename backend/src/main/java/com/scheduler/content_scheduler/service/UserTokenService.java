package com.scheduler.content_scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.content_scheduler.model.UserEntity;
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

    public void persistTokens(String platform, JsonNode token) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("User not authenticated");
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        userService.savePlatformToken(
                user,
                platform,
                token.get("access_token").asText(),
                token.has("refresh_token") ? token.get("refresh_token").asText() : null
        );
    }
}
