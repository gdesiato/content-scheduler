package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.dto.PostRequestDTO;
import com.scheduler.content_scheduler.dto.PostResponseDTO;
import com.scheduler.content_scheduler.model.PostStatus;
import com.scheduler.content_scheduler.service.PostService;
import com.scheduler.content_scheduler.validator.IdValidator;
import com.scheduler.content_scheduler.validator.PostRequestValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

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
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<PostResponseDTO>> getPostByStatus(@PathVariable PostStatus status) {
        List<PostResponseDTO> posts = postService.getPostsByStatus(status);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, PostRequestDTO updatedPostData) {
        PostResponseDTO post = postService.updatePostById(id, updatedPostData);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> schedulePost(@RequestBody PostRequestDTO postRequestDTO) {
        postRequestValidator.validate(postRequestDTO);
        PostResponseDTO post = postService.createPost(postRequestDTO);
        return ResponseEntity.ok(post);
    }
}
