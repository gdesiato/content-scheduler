package com.scheduler.content_scheduler.source;

import com.scheduler.content_scheduler.model.CanonicalPost;
import com.scheduler.content_scheduler.model.Platform;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface SourceAdapter {

    Platform platform();

    List<CanonicalPost> fetchNewPosts(User user);

}
