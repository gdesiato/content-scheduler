package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.model.ScheduledPost;
import com.scheduler.content_scheduler.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<ScheduledPost> schedulePost(@RequestBody ScheduledPost post) {
        ScheduledPost savedPost = postService.createPost(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public List<ScheduledPost> getAllPosts() {
        return postService.getAllPosts();
    }
}
