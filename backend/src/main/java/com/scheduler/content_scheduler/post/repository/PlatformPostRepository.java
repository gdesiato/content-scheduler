package com.scheduler.content_scheduler.post.repository;

import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.post.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlatformPostRepository extends JpaRepository<PlatformPost, UUID> {
    List<PlatformPost> findByPublishedFalseAndScheduledTimeBefore(Instant time);
    List<PlatformPost> findByStatus(PostStatus status);
}
