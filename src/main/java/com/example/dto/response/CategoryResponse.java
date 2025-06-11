package com.example.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(
        Integer id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}