package com.scheduler.content_scheduler.post.service;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.repository.CanonicalPostRepository;
import com.scheduler.content_scheduler.user.model.UserEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CanonicalPostService {

    private final CanonicalPostRepository canonicalPostRepository;

    public CanonicalPostService(CanonicalPostRepository canonicalPostRepository) {
        this.canonicalPostRepository = canonicalPostRepository;
    }

    public CanonicalPost getOrCreate(
            Platform sourcePlatform,
            String sourcePostId,
            UserEntity author,
            String content,
            Instant sourceCreatedAt
    ) {
        return canonicalPostRepository
                .findBySourcePlatformAndSourcePostId(sourcePlatform, sourcePostId)
                .orElseGet(() ->
                        canonicalPostRepository.save(
                                new CanonicalPost(
                                        sourcePlatform,
                                        sourcePostId,
                                        author,
                                        content,
                                        sourceCreatedAt
                                )
                        )
                );
    }
}

