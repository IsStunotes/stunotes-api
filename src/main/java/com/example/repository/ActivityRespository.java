package com.example.repository;

import com.example.model.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRespository extends JpaRepository<Activity, Integer> {
    Page<Activity> findByCategory(String categoryName, Pageable pageable);
    Page<Activity> findAllByOrderByPriorityAsc(Pageable pageable);
}

