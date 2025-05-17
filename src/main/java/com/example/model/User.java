package com.example.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference("user-teacher")
    private Teacher teacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference("user-student")
    private Student student;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name="role_id", referencedColumnName = "id")
    //private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @OneToOne (mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonBackReference("user-repositorio")
    private Repository repositorio;

    public void setCreatedAt(LocalDateTime now) {
    }
}
