package com.scheduler.content_scheduler.ingestion;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface SourceAdapter {

    Platform platform();

    List<CanonicalPost> fetchNewPosts(User user);

}
