package com.example.mapper;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import com.example.model.Comment;
import com.example.model.Document;
import com.example.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class CommentMapper {


    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                                comment.getId(),
                                comment.getContenido(),
                                comment.getFecha().toLocalDate().toString(),
                                comment.getDocument().getId(),
                                comment.getUsuario().getId()

        );
    }

    public Comment fromRequest(CommentRequest request, User user, Document document) {
        Comment comment = new Comment();
        comment.setContenido(request.contenido());
        comment.setUsuario(user);
        comment.setDocument(document);
        comment.setFecha(LocalDate.now().atStartOfDay());
        return comment;
    }
}
