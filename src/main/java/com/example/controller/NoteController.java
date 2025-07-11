package com.example.controller;

import com.example.dto.request.NoteRequest;
import com.example.dto.response.NoteResponse;
import com.example.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notes")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<Page<NoteResponse>> listNotes(
            @RequestParam(required = false) Integer user_id,
            @RequestParam(required = false) Integer collectionId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ){
        Sort sort = sortDirection.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (collectionId != null) {
            return ResponseEntity.ok(noteService.filterByCollection(collectionId, pageable));
        }
        if (searchTerm != null) {
            return ResponseEntity.ok(noteService.filterByTitleOrName(user_id,searchTerm, pageable));
        }

        return ResponseEntity.ok(noteService.paginate(user_id, pageable));
    }



    @GetMapping("/collection/{collection_id}")
    public ResponseEntity<List<NoteResponse>> listNotes(@PathVariable("collection_id") Integer collectionId) {
        return ResponseEntity.ok(noteService.filterByCollection(collectionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable("id") Integer id) {
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
