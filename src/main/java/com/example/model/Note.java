package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="content")
    private String content;

    @ManyToOne()
    @JoinColumn(name = "collection_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_notes_collections"))
    private Collection collection;
}
