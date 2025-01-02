package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.dto.HomePageDataDTO;
import com.scheduler.content_scheduler.dto.UserResponseDTO;
import com.scheduler.content_scheduler.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ResponseEntity<HomePageDataDTO> getHomePageData(Principal principal) {
        String username = principal.getName();
        UserResponseDTO user = userService.getUserByUsername(username);

        List<String> roles = List.of(user.role());

        HomePageDataDTO homePageDataDTO = new HomePageDataDTO(
                user.username(),
                roles,
                "Welcome, " + user.username() + "!"
        );
        return ResponseEntity.ok(homePageDataDTO);
    }

}
