package com.example.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;
import java.time.LocalDateTime;



@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;
    private LocalDateTime fecha;

    @ManyToOne
    @JsonBackReference
    private Document document;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

}
