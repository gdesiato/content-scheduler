package com.scheduler.content_scheduler.user.repository;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.user.model.UserPlatformToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPlatformTokenRepository extends JpaRepository<UserPlatformToken, Long> {
    Optional<UserPlatformToken> findByUserAndPlatform(
            UserEntity user,
            Platform platform
    );
}
