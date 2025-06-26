package com.example.repository;

import com.example.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Page<Note> findByCollection_Name(String collectionName, Pageable pageable);
    Page<Note> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
