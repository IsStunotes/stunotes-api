package com.example.repository;

import com.example.model.Activity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Page<Activity> findByCategory_Name(String categoryName, Pageable pageable);
    Page<Activity> findAllByOrderByPriorityAsc(Pageable pageable);
    Page<Activity> findByUserId(Integer userId, Pageable pageable);
    Page<Activity> findByUserIdAndCategory_Name(Integer userId, String categoryName, Pageable pageable);
    Page<Activity> findByUserIdOrderByPriority(Integer userId, Pageable pageable);
}

