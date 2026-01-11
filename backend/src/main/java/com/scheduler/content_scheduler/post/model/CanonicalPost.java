package com.scheduler.content_scheduler.post.model;

import com.scheduler.content_scheduler.user.model.UserEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "canonical_posts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_source_platform_post",
                        columnNames = {"source_platform", "source_post_id"}
                )
        }
)
public class CanonicalPost {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Platform where the content originated (e.g. MASTODON)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "source_platform", nullable = false, updatable = false)
    private Platform sourcePlatform;

    /**
     * ID of the original post on the source platform
     * Used for deduplication and traceability
     */
    @Column(name = "source_post_id", nullable = false, updatable = false)
    private String sourcePostId;

    /**
     * Internal user who owns this content
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private UserEntity author;

    /**
     * Canonical textual content (plain text or lightly normalized HTML)
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * When the post was originally created on the source platform
     */
    @Column(name = "source_created_at", nullable = false, updatable = false)
    private Instant sourceCreatedAt;

    /**
     * When the canonical post was ingested into our system
     */
    @Column(name = "ingested_at", nullable = false, updatable = false)
    private Instant ingestedAt = Instant.now();

    protected CanonicalPost() {}

    public CanonicalPost(
            Platform sourcePlatform,
            String sourcePostId,
            UserEntity author,
            String content,
            Instant sourceCreatedAt
    ) {
        this.sourcePlatform = sourcePlatform;
        this.sourcePostId = sourcePostId;
        this.author = author;
        this.content = content;
        this.sourceCreatedAt = sourceCreatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Platform getSourcePlatform() {
        return sourcePlatform;
    }

    public String getSourcePostId() {
        return sourcePostId;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Instant getSourceCreatedAt() {
        return sourceCreatedAt;
    }

    public Instant getIngestedAt() {
        return ingestedAt;
    }
}
