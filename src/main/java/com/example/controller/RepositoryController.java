package com.example.controller;

import com.example.model.entity.Repository;
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
    public List<Repository> getAllRepositories() {
        return repositoryService.getAllRepositories();
    }

    @PostMapping
    public Repository saveRepository(@RequestBody Repository repository) {
        return repositoryService.saveRepository(repository);
    }

    @GetMapping("/{id}")
    public Repository getRepositoryById(@PathVariable Long id) {
        return repositoryService.getRepositoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRepository(@PathVariable Long id) {
        repositoryService.deleteRepository(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Repository> getRepositoriesByUsuarioId(@PathVariable Long usuarioId) {
        return repositoryService.getRepositoriesByUsuarioId(usuarioId);
    }


}