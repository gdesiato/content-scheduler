package com.scheduler.content_scheduler.configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PkceUtil {

    private PkceUtil() {
        // utility class
    }

    /**
     * RFC 7636 - S256 code challenge
     */
    public static String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] digest = MessageDigest
                    .getInstance("SHA-256")
                    .digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

