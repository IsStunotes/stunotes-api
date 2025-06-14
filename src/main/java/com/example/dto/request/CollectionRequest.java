package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CollectionRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 255)
        String name
) { }
