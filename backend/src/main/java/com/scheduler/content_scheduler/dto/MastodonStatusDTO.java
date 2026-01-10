package com.scheduler.content_scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MastodonStatusDTO(String id, String content, String created_at) {}
