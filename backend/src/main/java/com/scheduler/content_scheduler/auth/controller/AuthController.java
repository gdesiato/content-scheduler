package com.scheduler.content_scheduler.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.content_scheduler.integrations.oauth.OAuthProvider;
import com.scheduler.content_scheduler.auth.service.AuthStateService;
import com.scheduler.content_scheduler.integrations.oauth.OAuthTokenService;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.service.UserTokenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.scheduler.content_scheduler.security.PkceUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Map<Platform, OAuthProvider> providers;
    private final AuthStateService authStateService;
    private final OAuthTokenService tokenService;
    private final UserTokenService userTokenService;


    public AuthController(
            List<OAuthProvider> authProviders,
            AuthStateService authStateService,
            OAuthTokenService tokenService,
            UserTokenService userTokenService
    ) {
        this.providers = authProviders.stream()
                .collect(Collectors.toMap(
                        OAuthProvider::getPlatform,
                        Function.identity()
                ));
        this.authStateService = authStateService;
        this.tokenService = tokenService;
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

        OAuthProvider provider = getProvider(platform);

        String state = authStateService.generateState();
        String codeVerifier = provider.usesPkce()
                ? authStateService.generateCodeVerifier()
                : null;

        String codeChallenge = codeVerifier != null
                ? PkceUtil.generateCodeChallenge(codeVerifier)
                : null;

        authStateService.storeState(state, codeVerifier);

        String url = provider.buildAuthorizationUrl(state, codeChallenge);
        return new RedirectView(url);
    }

    @GetMapping("/{platform}/callback")
    public String callback(
            @PathVariable Platform platform,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error
    ) throws IOException {

        if (error != null) {
            throw new RuntimeException("OAuth error: " + error);
        }
        if (code == null || state == null) {
            throw new RuntimeException("Missing code or state");
        }

        OAuthProvider provider = getProvider(platform);

        String codeVerifier = authStateService.getAndRemoveCodeVerifier(state);
        if (provider.usesPkce() && codeVerifier == null) {
            throw new RuntimeException("Invalid or expired state");
        }

        JsonNode token = tokenService.exchangeCode(provider, code, codeVerifier);

        userTokenService.persistTokens(platform, token);

        return "Authentication successful. You may close this window.";
    }

}
