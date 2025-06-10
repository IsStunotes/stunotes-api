package com.example.dto.request;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RepositoryRequest(

        @NotNull(message = "El usuario propietario del repositorio es obligatorio")
        Integer userId,
        List<DocumentRequest> documents
) {
}

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Long id;

//@OneToOne
//@JoinColumn(name = "usuario_id", nullable = false)
//@JsonManagedReference(value = "user-repositorio")
//private User usuario;

//@OneToMany (mappedBy = "repositorio", cascade = CascadeType.ALL)
//@JsonManagedReference
//private List<Document> documentos;

