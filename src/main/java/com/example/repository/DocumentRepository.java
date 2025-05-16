package com.example.repository;
import com.example.model.entity.Document;
import com.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface DocumentRepository  extends JpaRepository<Document, Long> {

    List<Document> findByRepositorioId(Long repositorioId);
    List<Document> findByUsuario(User usuario);


}
