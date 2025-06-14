package com.example.dto.response;

import java.time.LocalDateTime;

public record ActivityResponse (
    Integer id,
    String title,
    String description,
    LocalDateTime createdAt,
    LocalDateTime finishedAt,
    Integer priority,
    String categoryName
){}
