package com.example.service;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import com.example.dto.response.DocumentResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ActivityMapper;
import com.example.model.Activity;
import com.example.model.Category;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityMapper activityMapper;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> paginate(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<Activity> activities = activityRepository.findByUserId(user.getId(), pageable);
        return activities.map(activityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesByUserId(Pageable pageable) {
        User user = getAuthenticatedUser();
        return activityRepository.findByUserId(user.getId(), pageable)
                .map(activityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse findById(Integer id) {
        User user = getAuthenticatedUser();
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No puedes ver esta tarea");
        }

        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional
    public ActivityResponse create(ActivityRequest request) {
        User user = getAuthenticatedUser();

        Activity activity = activityMapper.toEntity(request);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
            activity.setCategory(category);
        }

        activity.setUser(user);
        activity.setCreatedAt(LocalDateTime.now());

        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(savedActivity);
    }

    @Override
    @Transactional
    public ActivityResponse update(Integer id, ActivityRequest request) {
        User user = getAuthenticatedUser();
        Activity activityFromDb = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        if (!activityFromDb.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No puedes editar esta tarea");
        }

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
        User user = getAuthenticatedUser();
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No puedes eliminar esta tarea");
        }

        activityRepository.delete(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> filterByCategory(String categoryName, Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<Activity> activities = activityRepository.findByUserIdAndCategory_Name(user.getId(), categoryName, pageable);
        return activities.map(activityMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> sortByPriority_withId(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<Activity> activities = activityRepository.findByUserIdOrderByPriority(user.getId(), pageable);
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
        User user = getAuthenticatedUser();
        Activity activity = activityRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No puedes completar esta tarea");
        }

        activity.setFinishedAt(LocalDateTime.now());
        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

}