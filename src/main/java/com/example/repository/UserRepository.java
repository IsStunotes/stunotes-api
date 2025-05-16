package com.example.repository;
import com.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepository  extends JpaRepository<User, Integer> {
    //Verifica si existe el email en registro
    boolean existsByEmail(String email);

    //Autenticación
    Optional<User> findByEmail(String email);
}
