package com.scheduler.content_scheduler.user.model;

import com.scheduler.content_scheduler.post.model.Platform;
import jakarta.persistence.*;

@Entity
@Table(
        name = "user_platform_tokens",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "platform"}
        )
)
public class UserPlatformToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(nullable = false, length = 2048)
    private String accessToken;

    private String refreshToken;

    // Mastodon-specific
    private String instanceUrl;

    public UserEntity getUser() {
        return user;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }
}

