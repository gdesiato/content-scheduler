package com.scheduler.content_scheduler.post.service;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.exception.PostNotFoundException;
import com.scheduler.content_scheduler.post.mapper.PostMapper;
import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.post.model.PostStatus;
import com.scheduler.content_scheduler.post.repository.PlatformPostRepository;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import twitter4j.Logger;
import twitter4j.Twitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private static final Logger log = Logger.getLogger(PostService.class);

    private final PlatformPostRepository repository;
    private final Twitter twitter;

    public PostService(PlatformPostRepository repository, Twitter twitter) {
        this.repository = repository;
        this.twitter = twitter;
    }

    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        PlatformPost post = PostMapper.toEntity(postRequestDTO);
        post.setPublished(false);
        post.setStatus(PostStatus.SCHEDULED);

        PlatformPost savedPost = repository.save(post);

        return PostMapper.toDTO(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        List<PlatformPost> posts = repository.findAll();
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public List<PostResponseDTO> getPostsByStatus(PostStatus status) {
        List<PlatformPost> posts = repository.findByStatus(status);
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public PlatformPost getPostById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));
    }

    public void postToPlatform(PlatformPost post) {
        Dotenv dotenv = Dotenv.configure().load();
        String bearerToken = dotenv.get("TWITTER_BEARER_TOKEN"); // Ensure this is set in .env

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String tweetContent = "{\"text\":\"" + post.getContent() + "\"}";

        RequestBody body = RequestBody.create(tweetContent, mediaType);

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets")
                .post(body)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to post tweet: " + response.body().string());
            }
            log.info("Tweet posted successfully: {}", response.body().string());
        } catch (IOException e) {
            throw new RuntimeException("Error posting tweet", e);
        }
    }


    public PostResponseDTO updatePostById(Long id, PostRequestDTO updatedPostData) {
        PlatformPost existingPost = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        existingPost.setContent(updatedPostData.content());
        existingPost.setPlatform(updatedPostData.platform());
        existingPost.setScheduledTime(updatedPostData.scheduledTime());

        PlatformPost updatedPost = repository.save(existingPost);

        return PostMapper.toDTO(updatedPost);
    }

    public void deletePost(Long id) {
        PlatformPost existingPost = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        repository.delete(existingPost);
    }

    /*
     * Process scheduled posts and publish them.
     * This method is triggered automatically at a fixed rate.
     */
    @Scheduled(fixedRate = 60000) // Runs every minute
    public void processScheduledPosts() {
        List<PlatformPost> posts = repository.findByIsPublishedFalseAndScheduledTimeBefore(LocalDateTime.now());
        posts.forEach(this::postToPlatform);
    }
}
