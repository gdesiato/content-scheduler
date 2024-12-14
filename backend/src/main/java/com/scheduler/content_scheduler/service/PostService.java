package com.scheduler.content_scheduler.service;

import com.scheduler.content_scheduler.dto.PostRequestDTO;
import com.scheduler.content_scheduler.dto.PostResponseDTO;
import com.scheduler.content_scheduler.exception.PostNotFoundException;
import com.scheduler.content_scheduler.mapper.PostMapper;
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

    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        Post post = PostMapper.toEntity(postRequestDTO);
        post.setPublished(false);
        post.setStatus(PostStatus.SCHEDULED);

        Post savedPost = repository.save(post);

        return PostMapper.toDTO(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = repository.findAll();
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public List<PostResponseDTO> getPostsByStatus(PostStatus status) {
        List<Post> posts = repository.findByStatus(status);
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));
        return PostMapper.toDTO(post);
    }

    public void postToPlatform(Post post) {
        post.setPublished(true);
        post.setStatus(PostStatus.POSTED);
        post.setPostedTime(LocalDateTime.now());
        repository.save(post);
    }

    public PostResponseDTO updatePostById(Long id, PostRequestDTO updatedPostData) {
        Post existingPost = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        existingPost.setContent(updatedPostData.content());
        existingPost.setPlatform(updatedPostData.platform());
        existingPost.setScheduledTime(updatedPostData.scheduledTime());

        Post updatedPost = repository.save(existingPost);

        return PostMapper.toDTO(updatedPost);
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
