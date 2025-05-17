package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference(value = "user-teacher")
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}

