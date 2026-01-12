package com.scheduler.content_scheduler.post.dto;

import java.time.Instant;

public record ReschedulePostRequestDTO(
        Instant scheduledTime
) {}
