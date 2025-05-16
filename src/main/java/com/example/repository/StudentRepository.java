package com.example.repository;

import com.example.model.entity.Group;
import com.example.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    //Registro
    boolean existsByNameAndLastName(String name, String lastName);

    Optional<Student> findByNameAndLastName(String name, String lastName);

    //Actualizacion de datos del estudiante
    boolean existsByNameAndLastNameAndUserIdNot(String name, String lastName, Integer userId);

    List<Student> findByGroup(Group group);
}
