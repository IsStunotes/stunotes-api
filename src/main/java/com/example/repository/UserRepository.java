package com.example.repository;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepository  extends JpaRepository<User, Integer> {
    //Verifica si existe el email en registro
    boolean existsByEmail(String email);


    Optional<User> findByEmailAndPassword(String email, String password);
}
