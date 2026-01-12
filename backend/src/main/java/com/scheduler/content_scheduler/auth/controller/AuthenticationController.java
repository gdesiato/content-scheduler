package com.scheduler.content_scheduler.auth.controller;

import com.scheduler.content_scheduler.auth.dto.AuthResponseDTO;
import com.scheduler.content_scheduler.auth.dto.LoginRequestDTO;
import com.scheduler.content_scheduler.security.JwtUtil;
import com.scheduler.content_scheduler.security.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public AuthResponseDTO authenticate(@Valid @RequestBody LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
                    )
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.username());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String token = jwtUtil.generateTokenWithRoles(request.username(), roles);

            return new AuthResponseDTO(token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
