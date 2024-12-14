package com.scheduler.content_scheduler.mapper;

import com.scheduler.content_scheduler.dto.PostRequestDTO;
import com.scheduler.content_scheduler.dto.PostResponseDTO;
import com.scheduler.content_scheduler.model.Post;

public class PostMapper {

    public static Post toEntity(PostRequestDTO dto) {
        Post post = new Post();
        post.setContent(dto.content());
        post.setPlatform(dto.platform());
        post.setScheduledTime(dto.scheduledTime());
        post.setPublished(false);
        return post;
    }

    public static PostResponseDTO toDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getPlatform(),
                post.getScheduledTime(),
                post.isPublished()
        );
    }
}
