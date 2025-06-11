package com.example.mapper;

import com.example.dto.request.RepositoryRequest;
import com.example.dto.response.RepositoryResponse;
import com.example.mapper.DocumentMapper;
import com.example.model.Repository;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RepositoryMapper {

    @Autowired
    private DocumentMapper documentMapper;

    public RepositoryResponse toResponse(Repository repository) {
        return new RepositoryResponse(
                repository.getId(),
                repository.getDocumentos() != null
                        ? repository.getDocumentos().stream()
                        .map(documentMapper::toResponse)
                        .collect(Collectors.toList())
                        : null
        );
    }

    public Repository fromRequest(RepositoryRequest request, User user) {
        Repository repository = new Repository();
        repository.setUsuario(user);

        return repository;
    }
}
