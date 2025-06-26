package com.example.controller;

import com.example.dto.request.DocumentRequest;
import com.example.dto.response.CommentResponse;
import com.example.dto.response.DocumentResponse;
import com.example.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // POST: Crear un documento
    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest request) {
        DocumentResponse response = documentService.saveDocument(request);
        return ResponseEntity.ok(response);
    }

    // GET: Obtener todos los documentos
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    // GET: Obtener documento por ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable Long id) {
        DocumentResponse response = documentService.getDocumentById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    // DELETE: Eliminar documento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Obtener comentarios por documento
    @GetMapping("/{documentId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByDocumentId(@PathVariable Long documentId) {
        return ResponseEntity.ok(documentService.getCommentsByDocumentId(documentId));
    }

    // GET: Obtener documentos por repositorio
    @GetMapping("/repositorio/{repositorioId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByRepositorioId(@PathVariable Long repositorioId) {
        return ResponseEntity.ok(documentService.getDocumentsByRepositorioId(repositorioId));
    }

    // Get: Obtener documentos por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(documentService.getDocumentsByUserId(userId));
    }
    // getDocumentByVersion
    @GetMapping("/{documentId}/version/{version}")
    public ResponseEntity<DocumentResponse> getDocumentByVersion(@PathVariable Long documentId, @PathVariable Long version) {
        DocumentResponse response = documentService.getDocumentByVersion(documentId, version);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    //borrar documento por version
    @DeleteMapping("/{documentId}/version/{version}")
    public ResponseEntity<Void> deleteDocumentByVersion(@PathVariable Long documentId, @PathVariable Long version) {
        documentService.deleteDocumentByVersion(documentId, version);
        return ResponseEntity.noContent().build();
    }
}
