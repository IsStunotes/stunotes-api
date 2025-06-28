package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderRequest (
        Long id, // Ya no es obligatorio
        @NotBlank(message = "El título del recordatorio es obligatorio") String titulo,
        Integer activityId, // Ya no es obligatorio
        @NotNull(message = "Fecha y hora son obligatorios") LocalDateTime dateTime
)
{}
