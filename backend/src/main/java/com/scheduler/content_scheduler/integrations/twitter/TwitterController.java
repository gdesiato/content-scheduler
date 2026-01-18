package com.scheduler.content_scheduler.integrations.twitter;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.user.service.UserService;
import com.scheduler.content_scheduler.user.service.UserTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/twitter")
public class TwitterController {

    private final TwitterService twitterService;
    private final UserService userService;
    private final UserTokenService userTokenService;

    public TwitterController(
            TwitterService twitterService,
            UserService userService,
            UserTokenService userTokenService
    ) {
        this.twitterService = twitterService;
        this.userService = userService;
        this.userTokenService = userTokenService;
    }

    @GetMapping("/me")
    public ResponseEntity<TwitterProfileDTO> me(Authentication auth) throws IOException {

        UserEntity user = userService.fromAuthentication(auth);
        String token = userTokenService.getAccessToken(user, Platform.TWITTER);

        return ResponseEntity.ok(twitterService.getMe(token));
    }
}
