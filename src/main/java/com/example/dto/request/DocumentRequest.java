package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Long repositoryId,
        @NotNull Integer userId,
        @NotNull Long version,
        LocalDateTime date_created,
        List<CommentRequest> comments



) {
}
