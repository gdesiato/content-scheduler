package com.scheduler.content_scheduler.post.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PlatformPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "canonical_post_id", nullable = false)
    private CanonicalPost canonicalPost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    private LocalDateTime scheduledTime;

    private boolean published;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private LocalDateTime postedTime;

    protected PlatformPost() {
        // JPA only
    }
    
    public PlatformPost(
            CanonicalPost canonicalPost,
            Platform platform,
            LocalDateTime scheduledTime
    ) {
        this.canonicalPost = canonicalPost;
        this.platform = platform;
        this.scheduledTime = scheduledTime;
        this.status = PostStatus.SCHEDULED;
        this.published = false;
    }

    // getters only where possible

    public Long getId() {
        return id;
    }

    public CanonicalPost getCanonicalPost() {
        return canonicalPost;
    }

    public Platform getPlatform() {
        return platform;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public boolean isPublished() {
        return published;
    }

    public PostStatus getStatus() {
        return status;
    }

    public LocalDateTime getPostedTime() {
        return postedTime;
    }

    public void setCanonicalPost(CanonicalPost canonicalPost) {
        this.canonicalPost = canonicalPost;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public void setPostedTime(LocalDateTime postedTime) {
        this.postedTime = postedTime;
    }

    // controlled state transitions
    public void markPublished() {
        this.published = true;
        this.status = PostStatus.PUBLISHED;
        this.postedTime = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = PostStatus.FAILED;
    }

    public void reschedule(LocalDateTime newScheduledTime) {
        if (this.published) {
            throw new IllegalStateException("Cannot reschedule a published post");
        }
        this.scheduledTime = newScheduledTime;
    }
}

