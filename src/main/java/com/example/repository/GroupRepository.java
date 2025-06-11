package com.example.repository;

import com.example.model.Group;
import com.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupRepository extends JpaRepository<Group,Integer> {
    List<Group> findByTeacherId(Integer userId);

}
