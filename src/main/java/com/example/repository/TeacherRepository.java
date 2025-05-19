package com.example.repository;

import com.example.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    //Registro
    boolean existsByNameAndLastName(String name, String lastName);

    Optional<Teacher> findByNameAndLastName(String name, String lastName);

    //Actualizacion de datos del estudiante
    boolean existsByNameAndLastNameAndUserIdNot(String name, String lastName, Integer userId);

}