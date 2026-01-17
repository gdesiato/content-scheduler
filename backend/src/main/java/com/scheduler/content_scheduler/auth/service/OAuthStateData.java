package com.scheduler.content_scheduler.auth.service;

import java.util.UUID;

public record OAuthStateData(
        String codeVerifier,
        UUID userId
) {}
