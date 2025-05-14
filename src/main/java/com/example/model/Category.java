package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="categories")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false, length=100)
    private String name;
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

}
