package com.scheduler.content_scheduler.model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@ToString
@RequiredArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private LocalDateTime scheduledTime;

    private boolean isPublished;

    @Enumerated(EnumType.STRING)
    private PostStatus status; // Differentiates between Scheduled and Posted

    // Additional metadata (optional, depending on your needs)
    private LocalDateTime postedTime;

    public void setPublished(boolean published) {
        this.isPublished = published;
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public LocalDateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalDateTime postedTime) {
        this.postedTime = postedTime;
    }
}
