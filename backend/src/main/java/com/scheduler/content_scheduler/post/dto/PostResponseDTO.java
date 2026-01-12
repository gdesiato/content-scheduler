package com.scheduler.content_scheduler.post.dto;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PostStatus;

import java.time.Instant;
import java.util.UUID;

public record PostResponseDTO(
        UUID platformPostId,
        UUID canonicalPostId,
        String content,
        Platform platform,
        Instant scheduledTime,
        PostStatus status
) {}
