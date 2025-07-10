package com.example.repository;

import com.example.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Page<Note> findByCollection_Id(Integer collectionId, Pageable pageable);

    Page<Note> findByCollection_User_Id(Integer userId, Pageable pageable);
    List<Note> findByCollection_Id(Integer collectionId);

    @Query("SELECT n FROM Note n JOIN Collection c ON n.collection.id = c.id "+
            "WHERE c.user.id = ?1 AND (LOWER(n.title) LIKE LOWER(CONCAT('%', ?2, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', ?2, '%')))")
    Page<Note> findByUserIdAndTitleOrCollectionName(Integer userId, String keyword, Pageable pageable);
}
