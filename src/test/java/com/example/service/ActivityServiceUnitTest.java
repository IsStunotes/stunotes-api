package com.example.service;

import com.example.dto.request.ActivityRequest;
import com.example.dto.response.ActivityResponse;
import com.example.mapper.ActivityMapper;
import com.example.model.Activity;
import com.example.model.Category;
import com.example.repository.ActivityRespository;
import com.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActivityServiceUnitTest {

    @Mock
    private ActivityRespository activityRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity activity;
    private ActivityRequest activityRequest;
    private ActivityResponse activityResponse;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1);
        category.setName("Estudios");

        activity = new Activity();
        activity.setId(1);
        activity.setTitle("Estudiar Java");
        activity.setDescription("Repasar conceptos de Spring Boot");
        activity.setPriority(1);
        activity.setCategory(category);
        activity.setCreatedAt(LocalDateTime.now());

        activityRequest = new ActivityRequest("Estudiar Java", "Repasar conceptos de Spring Boot",
                null, 1, 1);
        activityResponse = new ActivityResponse(1, "Estudiar Java",
                "Repasar conceptos de Spring Boot", LocalDateTime.now(), null,
                1, "Estudios");
    }

    @Test
    @DisplayName("CP01 - Crear tarea con datos válidos")
    void createActivity_validData_returnsCreated() {
        when(activityMapper.toEntity(activityRequest)).thenReturn(activity);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        ActivityResponse result = activityService.create(activityRequest);

        assertNotNull(result);
        assertEquals("Estudiar Java", result.title());
        verify(activityRepository).save(any(Activity.class));
        verify(categoryRepository).findById(1);
    }

    @Test
    @DisplayName("CP02 - Crear tarea con categoría inexistente")
    void createActivity_categoryNotFound_throwsException() {
        when(activityMapper.toEntity(activityRequest)).thenReturn(activity);
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.create(activityRequest));
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("CP03 - Crear tarea sin categoría")
    void createActivity_withoutCategory_returnsCreated() {
        ActivityRequest requestWithoutCategory = new ActivityRequest("Tarea sin categoría",
                "Descripción", null, 2, null);
        Activity activityWithoutCategory = new Activity();
        activityWithoutCategory.setTitle("Tarea sin categoría");
        ActivityResponse responseWithoutCategory = new ActivityResponse(1, "Tarea sin categoría",
                "Descripción", LocalDateTime.now(), null, 2, null);

        when(activityMapper.toEntity(requestWithoutCategory)).thenReturn(activityWithoutCategory);
        when(activityRepository.save(any(Activity.class))).thenReturn(activityWithoutCategory);
        when(activityMapper.toResponse(activityWithoutCategory)).thenReturn(responseWithoutCategory);

        ActivityResponse result = activityService.create(requestWithoutCategory);

        assertNotNull(result);
        assertEquals("Tarea sin categoría", result.title());
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("CP04 - Listar tareas con paginación")
    void paginateActivities_withData_returnsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Activity> page = new PageImpl<>(List.of(activity));
        Page<ActivityResponse> expectedPage = new PageImpl<>(List.of(activityResponse));

        when(activityRepository.findAll(pageable)).thenReturn(page);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        Page<ActivityResponse> result = activityService.paginate(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Estudiar Java", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("CP05 - Obtener tarea por ID válido")
    void findActivityById_found_returnsActivity() {
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        ActivityResponse result = activityService.findById(1);

        assertEquals("Estudiar Java", result.title());
        assertEquals("Estudios", result.categoryName());
    }

    @Test
    @DisplayName("CP06 - Obtener tarea inexistente")
    void findActivityById_notFound_throwsException() {
        when(activityRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.findById(99));
    }

    @Test
    @DisplayName("CP07 - Actualizar tarea con datos válidos")
    void updateActivity_validData_returnsUpdated() {
        ActivityRequest updateRequest = new ActivityRequest("Estudiar Spring",
                "Conceptos avanzados", null, 2, 1);
        Activity updatedActivity = new Activity();
        updatedActivity.setId(1);
        updatedActivity.setTitle("Estudiar Spring");
        ActivityResponse updatedResponse = new ActivityResponse(1, "Estudiar Spring",
                "Conceptos avanzados", LocalDateTime.now(), null, 2,
                "Estudios");

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(activityRepository.save(activity)).thenReturn(updatedActivity);
        when(activityMapper.toResponse(updatedActivity)).thenReturn(updatedResponse);

        ActivityResponse result = activityService.update(1, updateRequest);

        assertEquals("Estudiar Spring", result.title());
        verify(activityMapper).updateEntityFromRequest(activity, updateRequest);
    }

    @Test
    @DisplayName("CP08 - Actualizar tarea inexistente")
    void updateActivity_notFound_throwsException() {
        ActivityRequest updateRequest = new ActivityRequest("Nueva tarea",
                "Descripción", null, 1, 1);
        when(activityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.update(1, updateRequest));
    }

    @Test
    @DisplayName("CP09 - Actualizar tarea con categoría inexistente")
    void updateActivity_categoryNotFound_throwsException() {
        ActivityRequest updateRequest = new ActivityRequest("Tarea actualizada",
                "Descripción", null, 1, 99);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.update(1, updateRequest));
    }

    @Test
    @DisplayName("CP10 - Eliminar tarea existente")
    void deleteActivity_found_executesDelete() {
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        activityService.delete(1);

        verify(activityRepository).delete(activity);
    }

    @Test
    @DisplayName("CP11 - Eliminar tarea inexistente")
    void deleteActivity_notFound_throwsException() {
        when(activityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.delete(1));
    }

    @Test
    @DisplayName("CP12 - Filtrar tareas por categoría")
    void filterByCategory_withData_returnsFilteredPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Activity> filteredPage = new PageImpl<>(List.of(activity));

        when(activityRepository.findByCategory_Name("Estudios", pageable)).thenReturn(filteredPage);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        Page<ActivityResponse> result = activityService.filterByCategory("Estudios", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Estudios", result.getContent().get(0).categoryName());
    }

    @Test
    @DisplayName("CP13 - Filtrar tareas por categoría sin resultados")
    void filterByCategory_noResults_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Activity> emptyPage = new PageImpl<>(Collections.emptyList());

        when(activityRepository.findByCategory_Name("Inexistente", pageable)).thenReturn(emptyPage);

        Page<ActivityResponse> result = activityService.filterByCategory("Inexistente", pageable);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("CP14 - Ordenar tareas por prioridad")
    void sortByPriority_returnsOrderedPage() {
        Activity lowPriority = new Activity();
        lowPriority.setPriority(1);
        Activity highPriority = new Activity();
        highPriority.setPriority(3);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Activity> sortedPage = new PageImpl<>(Arrays.asList(lowPriority, highPriority));

        when(activityRepository.findAllByOrderByPriorityAsc(pageable)).thenReturn(sortedPage);
        when(activityMapper.toResponse(any(Activity.class))).thenReturn(activityResponse);

        Page<ActivityResponse> result = activityService.sortByPriority(pageable);

        assertEquals(2, result.getContent().size());
        verify(activityRepository).findAllByOrderByPriorityAsc(pageable);
    }

    @Test
    @DisplayName("CP15 - Marcar tarea como completada")
    void markAsCompleted_validId_returnsCompletedTask() {
        Activity completedActivity = new Activity();
        completedActivity.setId(1);
        completedActivity.setFinishedAt(LocalDateTime.now());
        ActivityResponse completedResponse = new ActivityResponse(1, "Estudiar Java",
                "Descripción", LocalDateTime.now(), LocalDateTime.now(), 1,
                "Estudios");

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenReturn(completedActivity);
        when(activityMapper.toResponse(completedActivity)).thenReturn(completedResponse);

        ActivityResponse result = activityService.markAsCompleted(1);

        assertNotNull(result.finishedAt());
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    @DisplayName("CP16 - Marcar tarea inexistente como completada")
    void markAsCompleted_notFound_throwsException() {
        when(activityRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.markAsCompleted(99));
    }
}