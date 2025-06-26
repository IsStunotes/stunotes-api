package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CollectionRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min= 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
        String name
) { }
