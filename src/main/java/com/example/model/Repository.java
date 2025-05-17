package com.example.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "repositorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonManagedReference(value = "user-repositorio")
    private User usuario;

    @OneToMany (mappedBy = "repositorio", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Document> documentos;

}
