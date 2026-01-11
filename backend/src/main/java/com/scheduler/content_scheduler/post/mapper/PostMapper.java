package com.scheduler.content_scheduler.post.mapper;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.post.dto.PostResponseDTO;
import com.scheduler.content_scheduler.post.model.PlatformPost;

public class PostMapper {

    public static PlatformPost toEntity(PostRequestDTO dto) {
        PlatformPost post = new PlatformPost();
        post.setContent(dto.content());
        post.setPlatform(dto.platform());
        post.setScheduledTime(dto.scheduledTime());
        post.setPublished(false);
        return post;
    }

    public static PostResponseDTO toDTO(PlatformPost post) {
        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getPlatform(),
                post.getScheduledTime(),
                post.isPublished()
        );
    }
}
