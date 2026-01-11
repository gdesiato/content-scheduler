package com.scheduler.content_scheduler.publisher;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.post.model.PlatformPost;

public interface PlatformPublisher {
    Platform supports();
    void publish(PlatformPost platformPost);
}

