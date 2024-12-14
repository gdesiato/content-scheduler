package com.scheduler.content_scheduler.validator;

public interface Validator<T> {
    void validate(T target);
}
