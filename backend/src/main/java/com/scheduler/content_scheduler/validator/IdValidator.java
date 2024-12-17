package com.scheduler.content_scheduler.validator;

import com.scheduler.content_scheduler.exception.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdValidator extends AbstractValidator<Long> {

    @Override
    protected void validate(Long id, List<ErrorMessage> errorMessages) {
        if (id == null || id <= 0) {
            errorMessages.add(new ErrorMessage("ID must be a positive number."));
        }
    }
}
