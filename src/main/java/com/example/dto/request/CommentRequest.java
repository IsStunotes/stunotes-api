package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record CommentRequest(
        @NotBlank(message = "El contenido del comentario es obligatorio")
        String contenido,

        @NotNull(message = "El ID del documento es obligatorio")
        Long documentId,

        @NotNull(message = "El ID del usuario es obligatorio")
        Integer userId

) {
}
