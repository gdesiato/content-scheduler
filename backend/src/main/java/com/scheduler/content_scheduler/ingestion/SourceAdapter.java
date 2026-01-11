package com.scheduler.content_scheduler.ingestion;

import com.scheduler.content_scheduler.post.model.CanonicalPost;
import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.model.UserEntity;

import java.util.List;

public interface SourceAdapter {
    Platform platform();
    List<CanonicalPost> fetchNewPosts(UserEntity user);
}
