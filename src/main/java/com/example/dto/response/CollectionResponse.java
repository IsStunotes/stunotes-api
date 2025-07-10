package com.example.dto.response;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record CollectionResponse(
        Integer id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer userId
) {
}
