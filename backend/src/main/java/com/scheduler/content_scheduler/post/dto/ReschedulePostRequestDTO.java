package com.scheduler.content_scheduler.post.dto;

import java.time.LocalDateTime;

public record ReschedulePostRequestDTO(LocalDateTime scheduledTime) {}
