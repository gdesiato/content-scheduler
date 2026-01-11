package com.scheduler.content_scheduler.post.repository;

import com.scheduler.content_scheduler.post.model.Post;
import com.scheduler.content_scheduler.post.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledPostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime time);
    List<Post> findByStatus(PostStatus status);

}
