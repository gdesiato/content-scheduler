package com.scheduler.content_scheduler.post.controller;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.post.mapper.PostMapper;
import com.scheduler.content_scheduler.post.model.Post;
import com.scheduler.content_scheduler.post.model.PostStatus;
import com.scheduler.content_scheduler.post.service.PostService;
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

    private final PostService postService;
    private final PostRequestValidator postRequestValidator;
    private final IdValidator idValidator;

    public PostController(PostService postService,
                          PostRequestValidator postRequestValidator,
                          IdValidator idValidator) {
        this.postService = postService;
        this.postRequestValidator = postRequestValidator;
        this.idValidator = idValidator;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> postList = postService.getAllPosts();
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        idValidator.validate(id);
        Post post = postService.getPostById(id);

        PostResponseDTO postResponseDTO = PostMapper.toDTO(post);
        return ResponseEntity.ok(postResponseDTO);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<PostResponseDTO>> getPostByStatus(@PathVariable PostStatus status) {
        List<PostResponseDTO> posts = postService.getPostsByStatus(status);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, PostRequestDTO updatedPostData) {
        idValidator.validate(id);
        postRequestValidator.validate(updatedPostData);
        PostResponseDTO post = postService.updatePostById(id, updatedPostData);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        idValidator.validate(id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> schedulePost(@RequestBody PostRequestDTO postRequestDTO) {
        postRequestValidator.validate(postRequestDTO);
        PostResponseDTO post = postService.createPost(postRequestDTO);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/publish")
    public ResponseEntity<PostResponseDTO> createAndPublishPost(@RequestBody PostRequestDTO postRequestDTO) {
        log.info("Received request to publish post: {}", postRequestDTO);

        // Step 1: Create and save the post
        PostResponseDTO createdPost = postService.createPost(postRequestDTO);
        log.info("Post created successfully with ID: {}", createdPost.id());

        // Step 2: Fetch the actual Post entity for publishing
        Post post = postService.getPostById(createdPost.id());

        // Step 3: Publish the post to the platform
        postService.postToPlatform(post);
        log.info("Post published successfully with ID: {}", createdPost.id());

        // Step 4: Return the updated response (optional, if you want the latest state)
        return ResponseEntity.ok(createdPost);
    }
}
