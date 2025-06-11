package com.example.dto.response;
import java.util.List;

public record RepositoryResponse(
        Long id,
        List<DocumentResponse> documents


) {}

// @OneToMany (mappedBy = "repositorio", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Document> documentos;
//
//}