package com.example.service;

import com.example.model.Activity;
import com.example.repository.ActivityRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRespository activityRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<Activity> paginate(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Activity findById(Integer id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
    }

    @Override
    @Transactional
    public Activity create(Activity activity) {
        activity.setCreatedAt(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public Activity update(Integer id, Activity updateActivity) {
        Activity activityFromDb = findById(id);
        activityFromDb.setTitle(updateActivity.getTitle());
        activityFromDb.setDescription(updateActivity.getDescription());
        activityFromDb.setFinishedAt(updateActivity.getFinishedAt());
        activityFromDb.setPriority(updateActivity.getPriority());
        activityFromDb.setCategory(updateActivity.getCategory());
        return activityRepository.save(activityFromDb);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Activity activity = findById(id);
        activityRepository.delete(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activity> filterByCategory(String courseName, Pageable pageable) {
        return activityRepository.findByCategory_NameContainingIgnoreCase(courseName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activity> sortByPriority(Pageable pageable) {
        return activityRepository.findAllByOrderByPriorityAsc(pageable);
    }

    @Override
    @Transactional
    public Activity markAsCompleted(Integer taskId) {
        Activity activity = findById(taskId);
        activity.setFinishedAt(LocalDateTime.now());
        return activityRepository.save(activity);
    }

}

