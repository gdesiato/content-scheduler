package com.scheduler.content_scheduler.post.dto;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PostStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDTO(
        Long id,
        UUID canonicalPostId,
        String content,
        Platform platform,
        LocalDateTime scheduledTime,
        PostStatus status
) {}
