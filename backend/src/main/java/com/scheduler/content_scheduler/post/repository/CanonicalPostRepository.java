package com.scheduler.content_scheduler.post.repository;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CanonicalPostRepository extends JpaRepository<CanonicalPost, UUID> {
}
