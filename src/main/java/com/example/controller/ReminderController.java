package com.example.controller;


import com.example.model.Reminder;
import com.example.service.ReminderExportService;
import com.example.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reminder")
public class ReminderController {
    private final ReminderService reminderService;
    private final ReminderExportService reminderExportService;

    @GetMapping
    public ResponseEntity<List<Reminder>> listReminder() {
        return ResponseEntity.ok(reminderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable("id") Integer id){
        Reminder reminder = reminderService.findById(id);
        return new ResponseEntity<>(reminder, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reminder> createReminder (@RequestBody Reminder reminder){
        Reminder newReminder = reminderService.create(reminder);
        return new ResponseEntity<>(newReminder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateReminder (@PathVariable("id") Integer id, @RequestBody Reminder reminder){
        Reminder updateReminder = reminderService.update(id,reminder);
        return new ResponseEntity<>(updateReminder,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reminder> deleteReminder (@PathVariable("id") Integer id){
        reminderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @GetMapping("/{id}/export-pdf")
    public ResponseEntity<byte[]> exportReminderAsPdf(@PathVariable Integer id) {
        Reminder reminder = reminderService.findById(id);
        if (reminder == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream bis = reminderExportService.generatePdf(reminder);

        HttpHeaders headers = new HttpHeaders();
        String filename = "horario_" + reminder.getActivity().getTitle().replace(" ", "_") + "_" + reminder.getDateTime().toLocalDate() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}


