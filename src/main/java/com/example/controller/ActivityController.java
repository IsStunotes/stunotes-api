package com.example.controller;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import com.example.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
@PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
//@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = {"Authorization", "Content-Type"}, allowCredentials = "true", maxAge = 3600)
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> listTasks(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 15) Pageable pageable) {

        if (categoryName != null) {
            return ResponseEntity.ok(activityService.filterByCategory(categoryName, pageable));
        }

        if ("priority".equalsIgnoreCase(sort)) {
            return ResponseEntity.ok(activityService.sortByPriority_withId(pageable));
        }

        return ResponseEntity.ok(activityService.paginate(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getTask(@PathVariable Integer id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createTask(@RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActivityResponse> partialUpdate(@PathVariable Integer id, @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ActivityResponse> markAsCompleted(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(activityService.markAsCompleted(id));
    }
}
