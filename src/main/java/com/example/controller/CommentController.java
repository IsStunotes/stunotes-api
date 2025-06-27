package com.example.controller;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import com.example.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public CommentResponse saveComment(@RequestBody @Valid CommentRequest request) {
        return commentService.saveComment(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public CommentResponse getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @GetMapping("/document/{documentId}")
    public List<CommentResponse> getCommentsByDocumentId(@PathVariable Long documentId) {
        return commentService.getCommentsByDocumentId(documentId);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('TEACHER')")
    public List<CommentResponse> getCommentsByUsuarioId(@PathVariable Integer usuarioId) {
        return commentService.getCommentsByUsuarioId(usuarioId);
    }
}
