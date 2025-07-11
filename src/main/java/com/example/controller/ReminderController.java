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

    @PostMapping
    public ResponseEntity<ReminderResponse> create (@Valid @RequestBody ReminderRequest request){
        return ResponseEntity.ok(reminderService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAll(){
        return ResponseEntity.ok(reminderService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update (@PathVariable Long id, @Valid @RequestBody ReminderRequest request){
        return ResponseEntity.ok(reminderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        reminderService.delete(id);
        return ResponseEntity.noContent().build();
    }


}


