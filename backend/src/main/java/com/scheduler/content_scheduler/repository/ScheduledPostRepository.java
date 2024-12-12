package com.scheduler.content_scheduler.repository;

import com.scheduler.content_scheduler.model.ScheduledPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledPostRepository extends JpaRepository<ScheduledPost, Long> {
    List<ScheduledPost> findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime time);
}
