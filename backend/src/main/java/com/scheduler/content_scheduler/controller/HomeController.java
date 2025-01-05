package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.dto.HomePageDataDTO;
import com.scheduler.content_scheduler.dto.UserResponseDTO;
import com.scheduler.content_scheduler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);


    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ResponseEntity<HomePageDataDTO> getHomePageData(Principal principal) {
        String username = principal.getName();
        log.info("Username from Principal: {}", username);

        UserResponseDTO user = userService.getUserByUsername(username);
        log.info("Roles: {}", List.of(user.role()));

        List<String> roles = List.of(user.role());

        HomePageDataDTO homePageDataDTO = new HomePageDataDTO(
                user.username(),
                roles,
                "Welcome, " + user.username() + "!"
        );
        return ResponseEntity.ok(homePageDataDTO);
    }

}
