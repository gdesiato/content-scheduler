package com.scheduler.content_scheduler.user.repository;

import com.scheduler.content_scheduler.user.model.UserPlatformToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlatformTokenRepository extends JpaRepository<UserPlatformToken, Long> {
}
