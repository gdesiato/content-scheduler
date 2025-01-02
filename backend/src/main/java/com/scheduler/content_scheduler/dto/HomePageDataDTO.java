package com.scheduler.content_scheduler.dto;

import java.util.List;

public record HomePageDataDTO(
        String username,
        List<String> roles,
        String welcomeMessage) {
}

