package com.scheduler.content_scheduler.service;

import com.scheduler.content_scheduler.model.ScheduledPost;
import com.scheduler.content_scheduler.repository.ScheduledPostRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final ScheduledPostRepository repository;

    public PostService(ScheduledPostRepository repository) {
        this.repository = repository;
    }

    public ScheduledPost createPost(ScheduledPost post) {
        post.setPublished(false);
        return repository.save(post);
    }

    public List<ScheduledPost> getAllPosts() {
        return repository.findAll();
    }

    public void postToPlatform(ScheduledPost post) {
        // Logic to interact with Twitter or LinkedIn APIs
        System.out.println("Posting to: " + post.getPlatform());
        post.setPublished(true);
        repository.save(post);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void processScheduledPosts() {
        List<ScheduledPost> posts = repository.findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime.now());
        posts.forEach(this::postToPlatform);
    }
}
