package com.example.service;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import com.example.mapper.CommentMapper;
import com.example.model.Comment;
import com.example.model.Document;
import com.example.model.User;
import com.example.repository.CommentRepository;
import com.example.repository.DocumentRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentMapper commentMapper;

    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    public CommentResponse saveComment(CommentRequest request) {
        Document document = documentRepository.findById(request.documentId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comment comment = commentMapper.fromRequest(request, user, document);
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    public CommentResponse getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toResponse)
                .orElse(null); // puedes lanzar excepción si prefieres
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<CommentResponse> getCommentsByDocumentId(Long documentId) {
        return commentRepository.findByDocumentId(documentId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    public List<CommentResponse> getCommentsByUsuarioId(Integer usuarioId) {
        return commentRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
