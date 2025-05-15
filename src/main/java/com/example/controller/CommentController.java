package com.example.controller;
import com.example.model.Comment;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }
    @PostMapping
    public Comment saveComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
    @GetMapping("/document/{documentId}")
    public List<Comment> getCommentsByDocumentId(@PathVariable Long documentId) {
        return commentService.getCommentsByDocumentId(documentId);
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Comment> getCommentsByUsuarioId(@PathVariable Long usuarioId) {
        return commentService.getCommentsByUsuarioId(usuarioId);
    }
}
