package com.scheduler.content_scheduler.validator;

import com.scheduler.content_scheduler.dto.PostRequestDTO;
import com.scheduler.content_scheduler.exception.ErrorMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostRequestValidator extends AbstractValidator<PostRequestDTO> {

    @Override
    public void validate(PostRequestDTO postRequestDTO, List<ErrorMessage> errorMessages) {
        if (postRequestDTO.content() == null || postRequestDTO.content().isBlank()) {
            errorMessages.add(new ErrorMessage("Content cannot be blank or null."));
        } else if (postRequestDTO.content().length() > 500) {
            errorMessages.add(new ErrorMessage("Content must not exceed 500 characters."));
        }

        if (postRequestDTO.platform() == null) {
            errorMessages.add(new ErrorMessage("Platform cannot be null."));
        }

        if (postRequestDTO.scheduledTime() == null) {
            errorMessages.add(new ErrorMessage("Scheduled time cannot be null."));
        } else if (postRequestDTO.scheduledTime().isBefore(LocalDateTime.now())) {
            errorMessages.add(new ErrorMessage("Scheduled time must be in the future or present."));
        }
    }
}
