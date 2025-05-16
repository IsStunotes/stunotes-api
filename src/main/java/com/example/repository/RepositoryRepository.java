package com.example.repository;
import com.example.model.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryRepository  extends JpaRepository<Repository, Long> {
    List<Repository> findByUsuarioId(Long usuarioId);
}
