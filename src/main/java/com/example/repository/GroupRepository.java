package com.example.repository;

import com.example.model.entity.Group;
import com.example.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupRepository extends JpaRepository<Group,Integer> {
    List<Group> findByTeacherId(Integer userId);

}
