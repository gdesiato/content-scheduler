package com.scheduler.content_scheduler.user.model;

import com.scheduler.content_scheduler.post.model.Platform;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "user_platform_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_platform",
                        columnNames = {"user_id", "platform"}
                )
        }
)
public class UserPlatformToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Platform platform;

    @Column(nullable = false, length = 2048)
    private String accessToken;

    @Column(length = 2048)
    private String refreshToken;

    // Mastodon-specific
    private String instanceUrl;

    protected UserPlatformToken() {}

    public UserPlatformToken(UserEntity user, Platform platform) {
        this.user = user;
        this.platform = platform;
    }

    // getters only (no setters for identity)

    public UUID getId() {
        return id;
    }

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

    // controlled mutation
    public void updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }
}

