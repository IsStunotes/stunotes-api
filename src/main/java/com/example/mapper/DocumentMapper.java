package com.example.mapper;

import com.example.dto.request.CommentRequest;
import com.example.dto.request.DocumentRequest;
import com.example.dto.response.CommentResponse;
import com.example.dto.response.DocumentResponse;
import com.example.model.Comment;
import com.example.model.Document;
import com.example.model.Repository;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    @Autowired
    private CommentMapper commentMapper;


    public DocumentResponse toResponse(Document document) {
        List<CommentResponse> commentResponses = document.getComments() != null
                ? document.getComments().stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList())
                : List.of();

        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                document.getRepositorio().getId(),
                document.getUsuario().getId(),
                document.getVersion(),
                document.getDate_created(),
                commentResponses
        );
    }

    public Document fromRequest(DocumentRequest request, User user, Repository repository) {
        Document document = new Document();
        document.setTitle(request.title());
        document.setDescription(request.description());
        document.setRepositorio(repository);
        document.setUsuario(user);
        document.setVersion(request.version());
        document.setDate_created(request.date_created() != null ? request.date_created() : LocalDateTime.now());

        return document;
    }
}
