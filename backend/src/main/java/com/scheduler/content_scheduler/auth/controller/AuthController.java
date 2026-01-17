package com.scheduler.content_scheduler.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.content_scheduler.auth.service.OAuthStateData;
import com.scheduler.content_scheduler.integrations.oauth.OAuthProvider;
import com.scheduler.content_scheduler.auth.service.AuthStateService;
import com.scheduler.content_scheduler.integrations.oauth.OAuthTokenService;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.user.service.UserService;
import com.scheduler.content_scheduler.user.service.UserTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.scheduler.content_scheduler.security.PkceUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final Map<Platform, OAuthProvider> providers;
    private final AuthStateService authStateService;
    private final OAuthTokenService tokenService;
    private final UserService userService;
    private final UserTokenService userTokenService;


    public AuthController(
            List<OAuthProvider> authProviders,
            AuthStateService authStateService,
            OAuthTokenService tokenService, UserService userService,
            UserTokenService userTokenService
    ) {
        this.providers = authProviders.stream()
                .collect(Collectors.toMap(
                        OAuthProvider::getPlatform,
                        Function.identity()
                ));
        this.authStateService = authStateService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.userTokenService = userTokenService;
    }

    private OAuthProvider getProvider(Platform platform) {
        OAuthProvider provider = providers.get(platform);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        return provider;
    }

    @GetMapping("/{platform}/authorize")
    public RedirectView authorize(@PathVariable Platform platform) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() instanceof String) {
            throw new IllegalStateException("User must be logged in to connect OAuth");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        UUID userId = user.getId();

        OAuthProvider provider = getProvider(platform);

        String state = authStateService.generateState();
        String codeVerifier = provider.usesPkce()
                ? authStateService.generateCodeVerifier()
                : null;

        authStateService.storeState(state, codeVerifier, userId);

        String codeChallenge = codeVerifier != null
                ? PkceUtil.generateCodeChallenge(codeVerifier)
                : null;

        String url = provider.buildAuthorizationUrl(state, codeChallenge);
        return new RedirectView(url);
    }

    @GetMapping("/{platform}/callback")
    public String callback(
            @PathVariable Platform platform,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletRequest request
    ) throws IOException {

        log.info(
                "OAuth CALLBACK hit platform={} query={}",
                platform,
                request.getQueryString()
        );

        log.info(
                "OAuth CALLBACK params code={} state={} error={}",
                code, state, error
        );

        if (error != null) {
            throw new RuntimeException("OAuth error: " + error);
        }
        if (code == null || state == null) {
            throw new RuntimeException("Missing code or state");
        }

        OAuthProvider provider = getProvider(platform);

        // 🔑 Restore OAuth state (THIS is the key change)
        OAuthStateData stateData = authStateService.consumeState(state);

        String codeVerifier = stateData.codeVerifier();
        UUID userId = stateData.userId();
        UserEntity user = userService.findById(userId);

        JsonNode token = tokenService.exchangeCode(provider, code, codeVerifier);

        userTokenService.persistTokensForUser(user, platform, token);

        return "Authentication successful. You may close this window.";
    }
}
