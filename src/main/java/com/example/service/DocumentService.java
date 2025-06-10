package com.example.service;

import com.example.dto.request.DocumentRequest;
import com.example.dto.response.CommentResponse;
import com.example.dto.response.DocumentResponse;
import com.example.mapper.DocumentMapper;
import com.example.model.Document;
import com.example.model.Repository;
import com.example.model.User;
import com.example.repository.DocumentRepository;
import com.example.repository.RepositoryRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private DocumentMapper documentMapper;

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    public DocumentResponse saveDocument(DocumentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Repository repository = repositoryRepository.findById(request.repositoryId())
                .orElseThrow(() -> new RuntimeException("Repositorio no encontrado"));

        //evitar duplicado de documentos por el titulo
        if (documentRepository.existsByTitleAndRepositorio_Id(request.title(), request.repositoryId())) {
            throw new RuntimeException("Ya existe un documento con el mismo título en este repositorio");
        }

        Document document = documentMapper.fromRequest(request, user, repository);
        Document saved = documentRepository.save(document);
        return documentMapper.toResponse(saved);
    }

    public DocumentResponse getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(documentMapper::toResponse)
                .orElse(null);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public List<CommentResponse> getCommentsByDocumentId(Long documentId) {
        return documentRepository.findById(documentId)
                .map(documentMapper::toResponse)
                .map(DocumentResponse::comments)
                .orElse(List.of());
    }

    public List<DocumentResponse> getDocumentsByRepositorioId(Long repositorioId) {
        return documentRepository.findByRepositorioId(repositorioId)
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }
    //GET DOCUMENT POR ID DE USUARIO
    public List<DocumentResponse> getDocumentsByUserId(Integer userId) {
        return documentRepository.findByUsuarioId(userId)
                .stream()
                .map(documentMapper::toResponse)
                .sorted(Comparator.comparing(DocumentResponse::dateCreated).reversed())
                .toList();
    }
    //get documento por version
    public DocumentResponse getDocumentByVersion(Long documentId, Long version) {
        return documentRepository.findByIdAndVersion(documentId, version)
                .stream()
                .findFirst()
                .map(documentMapper::toResponse)
                .orElse(null);
    }


    public void deleteDocumentByVersion(Long documentId, Long version) {
        Document document = documentRepository.findByIdAndVersion(documentId, version)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con la versión especificada"));

        documentRepository.delete(document);
    }
}
