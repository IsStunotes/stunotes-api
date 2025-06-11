package com.example.controller;

import com.example.dto.request.RepositoryRequest;
import com.example.dto.response.RepositoryResponse;
import com.example.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping
    public List<RepositoryResponse> getAllRepositories() {
        return repositoryService.getAllRepositories();
    }

    @PostMapping
    public RepositoryResponse saveRepository(@RequestBody RepositoryRequest request) {
        return repositoryService.saveRepository(request);
    }

    @GetMapping("/{id}")
    public RepositoryResponse getRepositoryById(@PathVariable Long id) {
        return repositoryService.getRepositoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRepository(@PathVariable Long id) {
        repositoryService.deleteRepository(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<RepositoryResponse> getRepositoriesByUsuarioId(@PathVariable Integer usuarioId) {
        return repositoryService.getRepositoriesByUsuarioId(usuarioId);
    }
}
