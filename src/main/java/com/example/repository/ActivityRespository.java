package com.example.repository;

import com.example.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRespository extends JpaRepository<Activity, Integer> {
    Page<Activity> findByCategory_NameContainingIgnoreCase(String filter, Pageable pageable);
    Page<Activity> findAllByOrderByPriorityAsc(Pageable pageable);
}

