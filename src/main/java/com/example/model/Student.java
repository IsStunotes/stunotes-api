package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name",nullable = false,length = 50)
    private String name;
    @Column(name="lastName",nullable = false,length = 50)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne
    @JsonManagedReference(value = "user-student")
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
