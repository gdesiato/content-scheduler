package com.scheduler.content_scheduler.exception;

public record ApiError(
        String code,
        String message
) {}
