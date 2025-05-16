package com.example.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity

public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name",nullable = false,length = 50)
    private String name;
    @Column(name="lastName",nullable = false,length = 50)
    private String lastName;

    @OneToMany(mappedBy = "teacher")
    private List<Group> groups;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}

