package com.example.controller;


import com.example.dto.request.ReminderRequest;
import com.example.dto.response.ReminderResponse;
import com.example.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reminder")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
public class ReminderController {
    private final ReminderService reminderService;

    //Creacion
    @PostMapping
    public ResponseEntity<ReminderResponse> create (@Valid @RequestBody ReminderRequest request){
        return ResponseEntity.ok(reminderService.create(request));
    }

    //Actualizacion de reminder
    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update (@PathVariable Integer id, @Valid @RequestBody ReminderRequest request){
        return ResponseEntity.ok(reminderService.update(id, request));
    }

    //Consultar recordatorios para una semana
    @GetMapping("/week")
    public ResponseEntity<List<ReminderResponse>> getRemindersForWeek(
            @RequestParam("start") String startDateString) {

        LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ISO_DATE);
        List<ReminderResponse> reminders = reminderService.getRemindersForWeek(startDate);
        return ResponseEntity.ok(reminders);
    }

}


