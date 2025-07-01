package com.example.service;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
    Page<ActivityResponse> paginate(Pageable pageable);
    ActivityResponse findById(Integer id);
    ActivityResponse create(ActivityRequest request);
    ActivityResponse update(Integer id, ActivityRequest request);
    void delete(Integer id);

    Page<ActivityResponse> getActivitiesByUserId(Pageable pageable);

    Page<ActivityResponse> filterByCategory(String categoryName, Pageable pageable);
    Page<ActivityResponse> sortByPriority_withId(Pageable pageable);
    Page<ActivityResponse> sortByPriority(Pageable pageable);
    ActivityResponse markAsCompleted(Integer taskId);
}