package com.scheduler.content_scheduler.post.mapper;

import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.post.model.PlatformPost;

public class PostMapper {

    // this class is not meant to be instantiated
    private PostMapper() {}

    public static PostResponseDTO toDTO(PlatformPost post) {
        return new PostResponseDTO(
                post.getId(),
                post.getCanonicalPost().getId(),
                post.getCanonicalPost().getContent(),
                post.getPlatform(),
                post.getScheduledTime(),
                post.getStatus()
        );
    }
}
