package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    public UserRepository userRepository;

    @Test
    void userRepositoryShouldNotBeNull() {
        assertNotNull(userRepository, "UserRepository should be autowired");
    }
}
