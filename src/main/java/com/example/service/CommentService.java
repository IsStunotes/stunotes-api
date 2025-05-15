package com.example.service;
import com.example.model.Comment;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service

public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    public List<Comment> getCommentsByDocumentId(Long documentId) {
        return commentRepository.findByDocumentId(documentId);
    }
    public List<Comment> getCommentsByUsuarioId(Long usuarioId) {
        return commentRepository.findByUsuarioId(usuarioId);
    }
}
