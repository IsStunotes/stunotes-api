package com.example.controller;
 import com.example.model.entity.Comment;
 import com.example.model.entity.Document;
 import com.example.service.DocumentService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
 import java.util.List;
 @RestController
 @RequestMapping("/documents")

public class DocumentController {
        @Autowired
        private DocumentService documentService;

        @GetMapping
        public List<Document> getAllDocuments() {
            return documentService.getAllDocuments();
        }

        @PostMapping
        public Document saveDocument(@RequestBody Document document) {
            return documentService.saveDocument(document);
        }

        @GetMapping("/{id}")
        public Document getDocumentById(@PathVariable Long id) {
            return documentService.getDocumentById(id);
        }

        @DeleteMapping("/{id}")
        public void deleteDocument(@PathVariable Long id) {
            documentService.deleteDocument(id);
        }


        @GetMapping("/{documentId}/comments")
        public List<Comment> getCommentsByDocumentId(@PathVariable Long documentId) {
            return documentService.getCommentsByDocumentId(documentId);
        }
        @GetMapping("/repositorio/{repositorioId}")
        public List<Document> getDocumentsByRepositorioId(@PathVariable Long repositorioId) {
            return documentService.getDocumentsByRepositorioId(repositorioId);
        }



}
