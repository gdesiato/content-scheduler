package com.scheduler.content_scheduler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerification {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Bcrypt hash from the database
        String storedPasswordHash = "$2a$10$jFUXrblDAESRWAvJVu2PkeulQMqcHi45oSWE.RfbwqU3imtKmWEd2";
        // Plaintext password
        String rawPassword = "admin";

        // Verify if the raw password matches the stored hash
        boolean matches = encoder.matches(rawPassword, storedPasswordHash);
        System.out.println("Password matches: " + matches);
    }
}
