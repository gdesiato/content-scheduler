package com.scheduler.content_scheduler.mapper;

import com.scheduler.content_scheduler.dto.UserRequestDTO;
import com.scheduler.content_scheduler.dto.UserResponseDTO;
import com.scheduler.content_scheduler.model.UserEntity;

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
