-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================
-- Canonical posts
-- =========================
CREATE TABLE canonical_posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    source_platform TEXT NOT NULL,
    source_post_id TEXT NOT NULL,

    author_id UUID NOT NULL,

    content TEXT NOT NULL,

    source_created_at TIMESTAMPTZ NOT NULL,
    ingested_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uk_source_platform_post
        UNIQUE (source_platform, source_post_id)
);

-- =========================
-- Platform posts
-- =========================
CREATE TABLE platform_posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    canonical_post_id UUID NOT NULL,
    platform TEXT NOT NULL,

    scheduled_time TIMESTAMPTZ,
    posted_time TIMESTAMPTZ,

    published BOOLEAN NOT NULL DEFAULT false,
    status TEXT NOT NULL,

    CONSTRAINT fk_platform_post_canonical
        FOREIGN KEY (canonical_post_id)
        REFERENCES canonical_posts(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_canonical_platform
        UNIQUE (canonical_post_id, platform)
);

-- =========================
-- Helpful indexes
-- =========================
CREATE INDEX idx_platform_posts_status
    ON platform_posts(status);

CREATE INDEX idx_platform_posts_scheduled_time
    ON platform_posts(scheduled_time);
