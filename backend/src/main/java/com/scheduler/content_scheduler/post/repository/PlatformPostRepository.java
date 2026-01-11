package com.scheduler.content_scheduler.post.repository;

import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.post.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlatformPostRepository extends JpaRepository<PlatformPost, Long> {
    List<PlatformPost> findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime time);
    List<PlatformPost> findByStatus(PostStatus status);
}
