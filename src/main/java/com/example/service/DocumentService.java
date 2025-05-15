package com.example.service;
import com.example.model.Comment;
import com.example.model.Document;
import com.example.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.model.Usuario;
import java.util.List;
@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
    public List<Document> getDocumentsByUsuario(Usuario usuario) {
       return documentRepository.findByUsuario(usuario);
    }
    public List<Comment> getCommentsByDocumentId(Long documentId) {
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document != null) {
            return document.getComments();
        }
        return null;
    }
    public List<Document> getDocumentsByRepositorioId(Long repositorioId) {
        return documentRepository.findByRepositorioId(repositorioId);
    }

}
