package com.scheduler.content_scheduler.auth.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthStateService {

    private final Map<String, OAuthStateData> states = new ConcurrentHashMap<>();

    public String generateState() {
        return UUID.randomUUID().toString();
    }

    public String generateCodeVerifier() {
        return UUID.randomUUID().toString();
    }

    public void storeState(String state, String codeVerifier, UUID userId) {
        states.put(state, new OAuthStateData(codeVerifier, userId));
    }

    public OAuthStateData consumeState(String state) {
        OAuthStateData data = states.remove(state);
        if (data == null) {
            throw new IllegalStateException("Invalid or expired OAuth state");
        }
        return data;
    }
}

