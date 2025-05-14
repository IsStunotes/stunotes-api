package com.example.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name",nullable = false,length = 50)
    private String name;
    @Column(name="lastName",nullable = false,length = 50)
    private String lastName;
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
