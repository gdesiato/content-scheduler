package com.scheduler.content_scheduler.post.service;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.exception.PostNotFoundException;
import com.scheduler.content_scheduler.post.mapper.PostMapper;
import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.post.model.PostStatus;
import com.scheduler.content_scheduler.post.repository.PlatformPostRepository;
import com.scheduler.content_scheduler.publisher.PlatformPublisher;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlatformPostService {

    private final PlatformPostRepository platformPostRepository;
    private final CanonicalPostService canonicalPostService;
    private final Map<Platform, PlatformPublisher> publishers;

    public PlatformPostService(
            PlatformPostRepository platformPostRepository,
            CanonicalPostService canonicalPostService,
            List<PlatformPublisher> publishers
    ) {
        this.platformPostRepository = platformPostRepository;
        this.canonicalPostService = canonicalPostService;
        this.publishers = publishers.stream()
                .collect(Collectors.toMap(
                        PlatformPublisher::supports,
                        Function.identity()
                ));
    }

    @Transactional
    public PostResponseDTO createAndPublish(PostRequestDTO postRequestDTO) {
        CanonicalPost canonical = canonicalPostService.getById(postRequestDTO.canonicalPostId());

        PlatformPost platformPost = new PlatformPost(
                canonical,
                postRequestDTO.platform(),
                postRequestDTO.scheduledTime()
        );

        platformPostRepository.save(platformPost);

        publish(platformPost);

        return PostMapper.toDTO(platformPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        List<PlatformPost> posts = platformPostRepository.findAll();
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public List<PostResponseDTO> getPostsByStatus(PostStatus status) {
        List<PlatformPost> posts = platformPostRepository.findByStatus(status);
        return posts.stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public PlatformPost getPostById(UUID id) {
        return platformPostRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));
    }

    @Transactional
    public void publish(PlatformPost post) {
        PlatformPublisher publisher = publishers.get(post.getPlatform());

        if (publisher == null) {
            throw new IllegalStateException(
                    "No publisher registered for platform " + post.getPlatform()
            );
        }
        publisher.publish(post);

        post.markPublished();
        platformPostRepository.save(post);
    }

    @Transactional
    public PostResponseDTO schedule(PostRequestDTO dto) {
        CanonicalPost canonical = canonicalPostService.getById(dto.canonicalPostId());

        PlatformPost post = new PlatformPost(
                canonical,
                dto.platform(),
                dto.scheduledTime()
        );
        platformPostRepository.save(post);

        return PostMapper.toDTO(post);
    }

    @Transactional
    public PostResponseDTO reschedule(UUID id, Instant newScheduledTime) {
        PlatformPost post = platformPostRepository.findById(id)
                .orElseThrow(() ->
                        new PostNotFoundException("Post with ID " + id + " not found")
                );
        if (post.isPublished()) {
            throw new IllegalStateException(
                    "Cannot reschedule a published post"
            );
        }
        post.reschedule(newScheduledTime);

        platformPostRepository.save(post);

        return PostMapper.toDTO(post);
    }

    @Transactional
    public void deletePost(UUID id) {
        PlatformPost post = platformPostRepository.findById(id)
                .orElseThrow(() ->
                        new PostNotFoundException("Post with ID " + id + " not found")
                );
        if (post.isPublished()) {
            throw new IllegalStateException(
                    "Cannot delete a published post"
            );
        }
        platformPostRepository.delete(post);
    }

    /*
     * Process scheduled posts and publish them.
     * This method is triggered automatically at a fixed rate.
     */
    @Scheduled(fixedRate = 60000)
    public void processScheduledPosts() {
        platformPostRepository
                .findByPublishedFalseAndScheduledTimeBefore(Instant.now())
                .forEach(this::publish);
    }
}
