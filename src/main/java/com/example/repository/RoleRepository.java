package com.example.repository;

import com.example.model.Role;
import com.example.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(ERole name);
}

