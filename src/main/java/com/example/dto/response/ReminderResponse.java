package com.example.dto.response;

import java.time.LocalDateTime;


public record ReminderResponse (
        Long id,
        String titulo,
        String activityName,
        LocalDateTime dateTime
){
}
