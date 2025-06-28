package com.example.dto.request;

import jakarta.validation.constraints.NotNull;

public record NoteRequest(
        @NotNull(message = "El título no puede ser nulo")
        String title,
        @NotNull(message = "El contenido no puede ser nulo")
        String content,
        Integer collectionId
) {
}
