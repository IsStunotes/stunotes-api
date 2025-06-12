package com.example.service;

import com.example.dto.request.ReminderRequest;
import com.example.dto.response.ReminderResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ReminderMapper;
import com.example.model.Activity;
import com.example.model.Reminder;
import com.example.repository.ActivityRespository;
import com.example.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReminderServiceUnitTest {

    private ReminderRepository reminderRepository;
    private ActivityRespository activityRespository;
    private ReminderMapper reminderMapper;
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {

        reminderRepository = mock(ReminderRepository.class);
        activityRespository = mock(ActivityRespository.class);
        reminderMapper = mock(ReminderMapper.class);
        reminderService = new ReminderService(reminderRepository, activityRespository, reminderMapper);
    }

    @Test
    void testCreateReminder_Success() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(1));
        Activity activity = new Activity();
        Reminder reminder = new Reminder();
        ReminderResponse response = new ReminderResponse(1L, "Actividad", request.dateTime());

        when(activityRespository.findById(100)).thenReturn(Optional.of(activity));
        when(reminderMapper.toEntity(request, activity)).thenReturn(reminder);
        when(reminderRepository.save(reminder)).thenReturn(reminder);
        when(reminderMapper.toResponse(reminder)).thenReturn(response);

        ReminderResponse result = reminderService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(reminderRepository).save(reminder);
    }

    @Test
    void testCreateReminder_MissingFields_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, null, null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });
        assertEquals("Faltan completar datos", exception.getMessage());
    }

    @Test
    void testUpdateReminder_Success() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(1));
        Activity activity = new Activity();
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().plusDays(2));
        Reminder updatedReminder = new Reminder();
        ReminderResponse response = new ReminderResponse(1L, "Actividad", request.dateTime());

        when(reminderRepository.findById(1)).thenReturn(Optional.of(existingReminder));
        when(activityRespository.findById(100)).thenReturn(Optional.of(activity));
        when(reminderMapper.toEntity(request, activity)).thenReturn(updatedReminder);
        when(reminderRepository.save(updatedReminder)).thenReturn(updatedReminder);
        when(reminderMapper.toResponse(updatedReminder)).thenReturn(response);

        ReminderResponse result = reminderService.update(1, request);

        assertNotNull(result);
        verify(reminderRepository).save(updatedReminder);
    }

    @Test
    void testUpdateReminder_PastDate_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(1));
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(existingReminder));

        assertThrows(IllegalStateException.class, () -> reminderService.update(1, request));
    }

    @Test
    void testDeleteReminder_PastDate_ThrowsException() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        assertThrows(IllegalStateException.class, () -> reminderService.delete(1));
    }

    @Test
    void testDeleteReminder_Success() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().plusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        reminderService.delete(1);

        verify(reminderRepository).delete(reminder);
    }
}
