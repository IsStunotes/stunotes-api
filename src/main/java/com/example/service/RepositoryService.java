package com.example.service;
import com.example.model.entity.Repository;
import com.example.repository.RepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {
    @Autowired
    private RepositoryRepository repositoryRepository;

    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    public Repository saveRepository(Repository repository) {
        return repositoryRepository.save(repository);
    }

    public Repository getRepositoryById(Long id) {
        return repositoryRepository.findById(id).orElse(null);
    }

    public void deleteRepository(Long id) {
        repositoryRepository.deleteById(id);
    }

    public List<Repository> getRepositoriesByUsuarioId(Long usuarioId) {
        return repositoryRepository.findByUsuarioId(usuarioId);
    }


}
