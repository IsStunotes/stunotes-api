package com.example.repository;


import com.example.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

    //List<Collection> findAllByUser(Integer userId);
    boolean existsByNameAndUser_Id(String name, Integer userId);
    List<Collection> findAll();

    Page<Collection> getCollectionsByUser_Id(Pageable pageable, Integer user_id);

    List<Collection> getCollectionsByUser_Id(Integer userId);
}
