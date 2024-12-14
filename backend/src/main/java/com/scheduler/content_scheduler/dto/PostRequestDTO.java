package com.scheduler.content_scheduler.dto;

import com.scheduler.content_scheduler.model.Platform;

import java.time.LocalDateTime;

public record PostRequestDTO(String content, Platform platform, LocalDateTime scheduledTime) {
}

