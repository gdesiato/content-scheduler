package com.scheduler.content_scheduler.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    static {
        System.setProperty("env", "test");
    }

    @Test
    void shouldLoadSecretKey() {
        JwtUtil jwtUtil = new JwtUtil();
        assertNotNull(jwtUtil.getSecretKey(), "SECRET_KEY should not be null");
        System.out.println("Loaded SECRET_KEY: " + jwtUtil.getSecretKey());
    }
}
