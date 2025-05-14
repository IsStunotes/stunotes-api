package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name="description", columnDefinition = "TEXT")
    private String description;
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
    @Column(name = "priority")
    private Integer priority;
}
