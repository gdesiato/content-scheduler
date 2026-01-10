package com.scheduler.content_scheduler.controller;

import com.scheduler.content_scheduler.authentication.JwtUtil;
import com.scheduler.content_scheduler.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil, 
            CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/auth/login")
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Load user details to fetch roles
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // Generate JWT with roles
            return jwtUtil.generateTokenWithRoles(username, roles);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
