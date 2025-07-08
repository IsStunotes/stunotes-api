package com.example.dto.response;

import java.time.LocalDateTime;

public record NoteResponse(
        Integer id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer collectionId,
        String collectionName
) {
}
