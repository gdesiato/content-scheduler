package com.scheduler.content_scheduler.post.controller;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.post.dto.ReschedulePostRequestDTO;
import com.scheduler.content_scheduler.post.mapper.PostMapper;
import com.scheduler.content_scheduler.post.model.PlatformPost;
import com.scheduler.content_scheduler.post.model.PostStatus;
import com.scheduler.content_scheduler.post.service.PlatformPostService;
import com.scheduler.content_scheduler.validator.IdValidator;
import com.scheduler.content_scheduler.validator.PostRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    Logger log = LoggerFactory.getLogger(PostController.class);

    private final PlatformPostService platformPostService;
    private final PostRequestValidator postRequestValidator;
    private final IdValidator idValidator;

    public PostController(PlatformPostService platformPostService,
                          PostRequestValidator postRequestValidator,
                          IdValidator idValidator) {
        this.platformPostService = platformPostService;
        this.postRequestValidator = postRequestValidator;
        this.idValidator = idValidator;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> postList = platformPostService.getAllPosts();
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        idValidator.validate(id);
        PlatformPost post = platformPostService.getPostById(id);

        PostResponseDTO postResponseDTO = PostMapper.toDTO(post);
        return ResponseEntity.ok(postResponseDTO);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<PostResponseDTO>> getPostByStatus(@PathVariable PostStatus status) {
        List<PostResponseDTO> posts = platformPostService.getPostsByStatus(status);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<PostResponseDTO> reschedulePost(
            @PathVariable Long id,
            @RequestBody ReschedulePostRequestDTO request) {
        idValidator.validate(id);
        PostResponseDTO post = platformPostService.reschedule(id, request.scheduledTime());
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        idValidator.validate(id);
        platformPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> schedulePost(@RequestBody PostRequestDTO request) {
        postRequestValidator.validate(request);
        PostResponseDTO post = platformPostService.schedule(request);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/publish")
    public ResponseEntity<PostResponseDTO> publishPost(@RequestBody PostRequestDTO postRequestDTO) {
        log.info("Received request to publish post: {}", postRequestDTO);
        PostResponseDTO post = platformPostService.createAndPublish(postRequestDTO);
        return ResponseEntity.ok(post);
    }
}
