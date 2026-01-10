package com.scheduler.content_scheduler.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class AuthStateService {

    private final Cache<String, String> stateCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public String generateState() {
        return java.util.UUID.randomUUID().toString();
    }

    public String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public void storeState(String state, String codeVerifier) {
        if (codeVerifier != null) {
            stateCache.put(state, codeVerifier);
        }
    }

    public String getAndRemoveCodeVerifier(String state) {
        String verifier = stateCache.getIfPresent(state);
        if (verifier != null) {
            stateCache.invalidate(state); // Ensure one-time use
        }
        return verifier;
    }
}
