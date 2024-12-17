package com.scheduler.content_scheduler.validator;

import com.scheduler.content_scheduler.dto.PostRequestDTO;
import com.scheduler.content_scheduler.exception.ErrorMessage;
import com.scheduler.content_scheduler.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator<T> {

    protected abstract void validate(T request, List<ErrorMessage> errorMessages);

    public void validate(T request) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(request, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    protected abstract void validate(Long id, List<ErrorMessage> errorMessages);
}
