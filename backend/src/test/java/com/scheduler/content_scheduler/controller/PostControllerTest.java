package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    public PostService postService;

    @Autowired
    public MockMvc mockMvc;

//    @Test
//    void getAllPosts_ReturnsListOfPosts() throws Exception {
//
//        mockMvc.perform(get("/api/posts")) {
//
//        }
//    }

    @Test
    void getPostById() {
    }

    @Test
    void getPostByStatus() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void schedulePost() {
    }
}
