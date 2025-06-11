package com.example.repository;


import com.example.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
}
