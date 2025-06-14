package com.example.service;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ActivityMapper;
import com.example.model.Activity;
import com.example.model.Category;
import com.example.repository.ActivityRepository;
import com.example.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> paginate(Pageable pageable) {
        Page<Activity> activities = activityRepository.findAll(pageable);
        return activities.map(activityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse findById(Integer id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional
    public ActivityResponse create(ActivityRequest request) {
        Activity activity = activityMapper.toEntity(request);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
            activity.setCategory(category);
        }

        activity.setCreatedAt(LocalDateTime.now());
        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(savedActivity);
    }

    @Override
    @Transactional
    public ActivityResponse update(Integer id, ActivityRequest request) {
        Activity activityFromDb = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        activityMapper.updateEntityFromRequest(activityFromDb, request);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
            activityFromDb.setCategory(category);
        }

        Activity updatedActivity = activityRepository.save(activityFromDb);
        return activityMapper.toResponse(updatedActivity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        activityRepository.delete(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> filterByCategory(String categoryName, Pageable pageable) {
        Page<Activity> activities = activityRepository.findByCategory_Name(categoryName, pageable);
        return activities.map(activityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> sortByPriority(Pageable pageable) {
        Page<Activity> activities = activityRepository.findAllByOrderByPriorityAsc(pageable);
        return activities.map(activityMapper::toResponse);
    }

    @Override
    @Transactional
    public ActivityResponse markAsCompleted(Integer taskId) {
        Activity activity = activityRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        activity.setFinishedAt(LocalDateTime.now());
        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }
}