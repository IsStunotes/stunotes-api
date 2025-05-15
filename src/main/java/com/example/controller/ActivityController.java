package com.example.controller;

import com.example.model.Activity;
import com.example.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<Page<Activity>> listTasks(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 15) Pageable pageable) {

        if (filter != null) {
            return ResponseEntity.ok(activityService.filterByCategory(filter, pageable));
        }

        if ("priority".equalsIgnoreCase(sort)) {
            return ResponseEntity.ok(activityService.sortByPriority(pageable));
        }

        return ResponseEntity.ok(activityService.paginate(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getTask(@PathVariable Integer id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Activity> createTask(@RequestBody Activity activity) {
        return new ResponseEntity<Activity>(activityService.create(activity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateTask(@PathVariable Integer id, @RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.update(id, activity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Activity> markAsCompleted(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(activityService.markAsCompleted(id));
    }
}
