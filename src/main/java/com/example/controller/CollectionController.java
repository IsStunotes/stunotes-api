package com.example.controller;

import com.example.model.Collection;
import com.example.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/collections")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN')")
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Collection>> getAllByUserId(@PathVariable("userId") Integer userId) {
        List<Collection> collections = collectionService.getAllByUserId(userId);
        //return ResponseEntity.ok(collectionService.getAllByUserId(userId));
        return new ResponseEntity<List<Collection>>(collections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollectionById(@PathVariable("id") Integer id) {
        Collection collection = collectionService.findById(id);
        return new ResponseEntity<Collection>(collection, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) {
        Collection newCollection = collectionService.create(collection);
        return new ResponseEntity<Collection>(newCollection, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collection> updateCollection(@PathVariable("id") Integer id,
                                                       @RequestBody Collection collection) {
        Collection updateCollection = collectionService.create(collection);
        return new ResponseEntity<Collection>(updateCollection, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Collection> deleteCollection(@PathVariable("id") Integer id,
                                                       @RequestBody Collection collection) {
        collectionService.delete(id);
        return new ResponseEntity<Collection>(HttpStatus.NO_CONTENT);
    }


}
