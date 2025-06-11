package com.example.controller;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.model.Collection;
import com.example.service.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.collection.spi.CollectionSemanticsResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping()
    public ResponseEntity<List<CollectionResponse>> getAll() {
        return ResponseEntity.ok(collectionService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CollectionResponse>> getPaginated(Pageable pageable) {
        return ResponseEntity.ok(collectionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(collectionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CollectionResponse> create(@Valid @RequestBody CollectionRequest request) {
        CollectionResponse response = collectionService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionResponse> update(@PathVariable("id") Integer id,
                                             @RequestBody CollectionRequest request) {
        return ResponseEntity.ok(collectionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionResponse> delete(@PathVariable("id") Integer id) {
        collectionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
