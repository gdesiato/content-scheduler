package com.scheduler.content_scheduler.validator;

import com.scheduler.content_scheduler.post.dto.PostRequestDTO;
import com.scheduler.content_scheduler.exception.ErrorMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class PostRequestValidator extends AbstractValidator<PostRequestDTO> {

    @Override
    public void validate(PostRequestDTO dto, List<ErrorMessage> errors) {

        if (dto.canonicalPostId() == null) {
            errors.add(new ErrorMessage("Canonical post ID cannot be null."));
        }
        if (dto.platform() == null) {
            errors.add(new ErrorMessage("Platform cannot be null."));
        }
        if (dto.scheduledTime() == null) {
            errors.add(new ErrorMessage("Scheduled time cannot be null."));
        } else if (dto.scheduledTime().isBefore(Instant.now())) {
            errors.add(new ErrorMessage("Scheduled time must be in the future or present."));
        }
    }
}
