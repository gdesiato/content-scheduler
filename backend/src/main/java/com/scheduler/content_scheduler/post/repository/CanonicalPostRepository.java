package com.scheduler.content_scheduler.post.repository;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CanonicalPostRepository extends JpaRepository<CanonicalPost, UUID> {
    Optional<CanonicalPost> findBySourcePlatformAndSourcePostId(
            Platform sourcePlatform,
            String sourcePostId
    );
}
