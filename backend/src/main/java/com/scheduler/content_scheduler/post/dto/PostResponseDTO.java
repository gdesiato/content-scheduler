package com.scheduler.content_scheduler.post.dto;

import com.scheduler.content_scheduler.post.model.Platform;

import java.time.LocalDateTime;

public record PostResponseDTO(Long id,
                              String content,
                              Platform platform,
                              LocalDateTime scheduledTime,
                              boolean isPublished) {
}
