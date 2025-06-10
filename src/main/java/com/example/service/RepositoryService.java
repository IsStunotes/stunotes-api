package com.example.service;

import com.example.dto.request.RepositoryRequest;
import com.example.dto.response.RepositoryResponse;
import com.example.mapper.RepositoryMapper;
import com.example.model.Repository;
import com.example.model.User;
import com.example.repository.RepositoryRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepositoryMapper repositoryMapper;

    public List<RepositoryResponse> getAllRepositories() {
        return repositoryRepository.findAll()
                .stream()
                .map(repositoryMapper::toResponse)
                .toList();
    }

    public RepositoryResponse saveRepository(RepositoryRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Repository repository = repositoryMapper.fromRequest(request, user);
        Repository saved = repositoryRepository.save(repository);
        return repositoryMapper.toResponse(saved);
    }

    public RepositoryResponse getRepositoryById(Long id) {
        return repositoryRepository.findById(id)
                .map(repositoryMapper::toResponse)
                .orElse(null);
    }

    public void deleteRepository(Long id) {
        repositoryRepository.deleteById(id);
    }

    public List<RepositoryResponse> getRepositoriesByUsuarioId(Integer usuarioId) {
        return repositoryRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(repositoryMapper::toResponse)
                .toList();
    }
}
