package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderRequest (
    @NotNull Long id,
    @NotNull(message = "Debe ser obligatorio") Integer activityId,
    @NotNull(message = "Fecha y hora son obligatorios") LocalDateTime dateTime
)
{}
