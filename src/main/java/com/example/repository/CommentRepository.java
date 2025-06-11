package com.example.repository;
import com.example.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    List<Comment> findByDocumentId(Long documentId);
    List<Comment> findByUsuarioId(Integer usuarioId);

}
