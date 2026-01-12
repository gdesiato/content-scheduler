package com.scheduler.content_scheduler.post.dto;

import com.scheduler.content_scheduler.post.model.Platform;

import java.time.Instant;
import java.util.UUID;

public record PostRequestDTO(
        UUID canonicalPostId,
        Platform platform,
        Instant scheduledTime
) {}
