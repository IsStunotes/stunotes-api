package com.example.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentResponse(
        Long id,
        String title,
        String description,
        Long repositoryId,
        Integer userId,
        Long version,
        LocalDateTime dateCreated,
        List<CommentResponse> comments
) {
}
