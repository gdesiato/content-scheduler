package com.scheduler.content_scheduler.user.dto;

import java.util.UUID;

public record UserResponseDTO(UUID id, String username, String role) {
}
