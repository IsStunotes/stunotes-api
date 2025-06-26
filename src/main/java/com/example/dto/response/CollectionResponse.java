package com.example.dto.response;

import java.time.LocalDateTime;

public record CollectionResponse(
        Integer id,
        String name,
        LocalDateTime createdAt
) {
}
