package com.example.repository;

import com.example.model.Group;
import com.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    //Registro
    boolean existsByNameAndLastName(String name, String lastName);

    Optional<Student> findByNameAndLastName(String name, String lastName);

    //Si existe un cliente con mismo nombre y apellido, excepto el usuario actual
    boolean existsByNameAndLastNameAndUserIdNot(String name, String lastName, Integer userId);

    List<Student> findByGroup(Group group);
}
