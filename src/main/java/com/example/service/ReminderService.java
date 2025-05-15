package com.example.service;

import com.example.model.Reminder;

import java.util.List;

public interface ReminderService {
    List<Reminder> getAll();
    Reminder findById(Integer id);
    Reminder create(Reminder reminder);
    Reminder update(Integer id, Reminder reminder);
    void delete (Integer id);
}
