package com.example.service;

import com.example.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {
    List<Activity> getAll();
    Page<Activity> paginate(Pageable pageable);
    Activity findById(Integer id);
    Activity create(Activity activity);
    Activity update(Integer id, Activity updateActivity);
    void delete(Integer id);

    Page<Activity> filterByCategory(String courseName, Pageable pageable);
    Page<Activity> sortByPriority(Pageable pageable);
    Activity markAsCompleted(Integer taskId);
}
