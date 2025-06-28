package com.example.controller;

import com.example.dto.request.ActivityRequest;
import com.example.dto.request.NoteRequest;
import com.example.dto.response.ActivityResponse;
import com.example.dto.response.NoteResponse;
import com.example.service.ActivityService;
import com.example.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notes")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<Page<NoteResponse>> listNotes(
            @RequestParam(required = false) String collectionName,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 15) Pageable pageable
    ){
        if (collectionName != null) {
            return ResponseEntity.ok(noteService.filterByCollection(collectionName, pageable));
        }
        if ("updatedAt".equalsIgnoreCase(sort)) {
            return ResponseEntity.ok(noteService.sortByUpdatedAt(pageable));
        }
        return ResponseEntity.ok(noteService.paginate(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Integer id) {
        return ResponseEntity.ok(noteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request) {
        NoteResponse response = noteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteResponse> partialUpdate(@PathVariable Integer id, @RequestBody NoteRequest request) {
        NoteResponse response = noteService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotes(@PathVariable Integer id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
