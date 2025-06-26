package com.example.repository;


import com.example.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

    //List<Collection> findAllByUser(Integer userId);
    boolean existsByName(String name);
    Page<Collection> findAll(Pageable pageable);
}
