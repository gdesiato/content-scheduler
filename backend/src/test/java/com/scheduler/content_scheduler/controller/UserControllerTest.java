package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.authentication.JwtUtil;
import com.scheduler.content_scheduler.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public JwtUtil jwtUtil;

    @Test
    void userRepositoryShouldNotBeNull() {
        assertNotNull(userRepository, "UserRepository should be autowired");
    }

    @Test
    void getUsers_ShouldReturnUsers_WhenAdminTokenProvided() throws Exception {
        String adminToken = jwtUtil.generateTokenWithRoles("admin", List.of("ROLE_ADMIN"));

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.username == 'user1')]").exists())
                .andExpect(jsonPath("$[?(@.username == 'admin')]").exists());
    }
}
