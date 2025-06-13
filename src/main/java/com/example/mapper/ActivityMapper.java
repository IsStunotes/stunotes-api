package com.example.mapper;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import com.example.model.Activity;
import com.example.model.Category;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public Activity toEntity(ActivityRequest request) {
        Activity activity = new Activity();
        activity.setTitle(request.title());
        activity.setDescription(request.description());
        activity.setFinishedAt(request.finishedAt());
        activity.setPriority(request.priority());

        if (request.categoryId() != null) {
            Category category = new Category();
            category.setId(request.categoryId());
            activity.setCategory(category);
        }

        return activity;
    }

    public ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getCreatedAt(),
                activity.getFinishedAt(),
                activity.getPriority(),
                activity.getCategory() != null ? activity.getCategory().getName() : null
        );
    }
    public void updateEntityFromRequest(Activity activity, ActivityRequest request) {
        if (request.title() != null) {
            activity.setTitle(request.title());
        }
        if (request.description() != null) {
            activity.setDescription(request.description());
        }
        if (request.finishedAt() != null) {
            activity.setFinishedAt(request.finishedAt());
        }
        if (request.priority() != null) {
            activity.setPriority(request.priority());
        }
        if (request.categoryId() != null) {
            Category category = new Category();
            category.setId(request.categoryId());
            activity.setCategory(category);
        }
    }
}