package com.example.repository;

import com.example.model.Activity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;

public interface ActivityRespository extends JpaRepository<Activity, Integer> {
    Page<Activity> findByCategory_Name(String categoryName, Pageable pageable);
    Page<Activity> findAllByOrderByPriorityAsc(Pageable pageable);

    <T> ScopedValue<T> findById(@NotNull Long aLong);
}

