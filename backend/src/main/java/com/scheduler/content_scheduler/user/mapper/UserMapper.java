package com.scheduler.content_scheduler.user.mapper;

import com.scheduler.content_scheduler.user.dto.UserRequestDTO;
import com.scheduler.content_scheduler.user.dto.UserResponseDTO;
import com.scheduler.content_scheduler.user.model.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserRequestDTO dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        return user;
    }

    public static UserResponseDTO toDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
