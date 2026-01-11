package com.scheduler.content_scheduler.ingestion;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.repository.CanonicalPostRepository;
import com.scheduler.content_scheduler.user.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionService {

    private final List<SourceAdapter> adapters;
    private final CanonicalPostRepository repository;

    public IngestionService(
            List<SourceAdapter> adapters,
            CanonicalPostRepository repository
    ) {
        this.adapters = adapters;
        this.repository = repository;
    }

    public void ingest(UserEntity user) {
        for (SourceAdapter adapter : adapters) {
            List<CanonicalPost> posts = adapter.fetchNewPosts(user);
            repository.saveAll(posts);
        }
    }
}
