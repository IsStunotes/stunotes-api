package com.example.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ActivityRequest (
        @NotNull(message = "El título no puede ser nulo")
    String title,
    @NotNull(message = "La descripción no puede ser nula")
    String description,
    LocalDateTime finishedAt,
    Integer priority,
    Integer categoryId
) {}
