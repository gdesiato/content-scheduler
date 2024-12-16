package com.scheduler.content_scheduler;

import java.util.UUID;

public class TestHelperMethods {

    public static String generateRandomName() {
        return "user" + UUID.randomUUID().toString().substring(0, 8);
    }
}
