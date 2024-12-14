package com.scheduler.content_scheduler.service;

import com.scheduler.content_scheduler.exception.PostNotFoundException;
import com.scheduler.content_scheduler.model.Post;
import com.scheduler.content_scheduler.model.PostStatus;
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

    public Post createPost(Post post) {
        post.setPublished(false);
        post.setStatus(PostStatus.SCHEDULED);
        return repository.save(post);
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public List<Post> getPostsByStatus(PostStatus status) {
        return repository.findByStatus(status);
    }

    public Post getPostById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));
    }

    public void postToPlatform(Post post) {
        post.setPublished(true);
        post.setStatus(PostStatus.POSTED);
        post.setPostedTime(LocalDateTime.now());
        repository.save(post);
    }

    public Post updatePostById(Long id, Post updatedPostData) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        existingPost.setContent(updatedPostData.getContent());
        existingPost.setPlatform(updatedPostData.getPlatform());
        existingPost.setScheduledTime(updatedPostData.getScheduledTime());

        return repository.save(existingPost);
    }

    public void deletePost(Long id) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        repository.delete(existingPost);
    }

    /*
     * Process scheduled posts and publish them.
     * This method is triggered automatically at a fixed rate.
     */
    @Scheduled(fixedRate = 60000) // Runs every minute
    public void processScheduledPosts() {
        List<Post> posts = repository.findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime.now());
        posts.forEach(this::postToPlatform);
    }
}
