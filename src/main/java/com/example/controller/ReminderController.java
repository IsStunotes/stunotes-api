package com.example.controller;


import com.example.dto.request.ReminderRequest;
import com.example.dto.response.ReminderResponse;
import com.example.model.Reminder;
import com.example.service.ReminderExportService;
import com.example.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reminder")
public class ReminderController {
    private final ReminderService reminderService;
    private final ReminderExportService reminderExportService;

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

}


