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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReminderServiceUnitTest {

    @Mock private ReminderRepository reminderRepository;
    @Mock private ActivityRespository activityRespository;
    @Mock private ReminderMapper reminderMapper;
    @InjectMocks private ReminderService reminderService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    // US07 - Crear recordatorios

    @Test
    @DisplayName("CP35 - Crear recordatorio válido")
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
    @DisplayName("CP36 - Crear recordatorio inválidos (datos faltantes)")
    void testCreateReminder_MissingFields_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, null, null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });
        assertEquals("Faltan completar datos", exception.getMessage());
    }

    @Test
    @DisplayName("CP37 - Crear recordatorio con fecha pasada")
    void testCreateReminder_PastDate_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().minusDays(1));
        Activity activity = new Activity();
        when(activityRespository.findById(100)).thenReturn(Optional.of(activity));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("La fecha del recordatorio debe ser futura", exception.getMessage());
    }

    @Test
    @DisplayName("CP38 - Crear recordatorio sin actividad")
    void testCreateReminder_NoActivityId_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, null, LocalDateTime.now().plusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("Faltan completar datos", exception.getMessage());
    }

    @Test
    @DisplayName("CP39 - Crear recordatorio con actividad no encontrada")
    void testCreateReminder_ActivityNotFound_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, 999, LocalDateTime.now().plusDays(1));
        when(activityRespository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("Actividad no encontrada", exception.getMessage());
    }



    // US12 - Editar y eliminar recordatorios

    @Test
    @DisplayName("CP - Editar recordatorio creado valido")
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
    @DisplayName("CP - Editar recordatorio creado invalido (datos faltantes)")
    void testUpdateReminder_PastDate_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(1));
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(existingReminder));

        assertThrows(IllegalStateException.class, () -> reminderService.update(1, request));
    }

    @Test
    @DisplayName("CP - Cancelar edición, no debe guardar cambios")
    void testCancelUpdate_DoesNotSaveChanges() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(2));
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().plusDays(3)); // Diferente

        when(reminderRepository.findById(1)).thenReturn(Optional.of(existingReminder));

        verify(reminderRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP - Editar recordatorio inexistente")
    void testUpdateReminder_NotFound_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, 100, LocalDateTime.now().plusDays(1));
        when(reminderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reminderService.update(1, request));
    }

    @Test
    @DisplayName("CP - Eliminar recordatorio")
    void testDeleteReminder_Success() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().plusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        reminderService.delete(1);

        verify(reminderRepository).delete(reminder);
    }

    @Test
    @DisplayName("CP - Eliminar recordatorio invalido  ")
    void testDeleteReminder_PastDate_ThrowsException() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        assertThrows(IllegalStateException.class, () -> reminderService.delete(1));
    }

    @Test
    @DisplayName("CP - Eliminar recordatorio que no existe")
    void testDeleteReminder_NotFound_ThrowsException() {
        when(reminderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reminderService.delete(1));
    }

    @Test
    @DisplayName("CP - Recuperar recordatorio eliminado no está permitido")
    void testGetDeletedReminder_ThrowsException() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().minusDays(2)); // Simula vencido

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        Exception exception = assertThrows(IllegalStateException.class, () -> reminderService.getById(1));

        assertEquals("El recordatorio ya venció y no se puede recuperar", exception.getMessage());
    }



    // US13 - Obtener recordatorio por id (parte del flujo exportar horario)

    @Test
    @DisplayName("CP - Obtener recordatorio por id valido")
    void testGetById_Success() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().plusDays(1));
        ReminderResponse response = new ReminderResponse(1L, "Actividad", reminder.getDateTime());

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));
        when(reminderMapper.toResponse(reminder)).thenReturn(response);

        ReminderResponse result = reminderService.getById(1);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(reminderRepository).findById(1);
    }

    @Test
    @DisplayName("CP - Obtener recordatorio por id invalido")
    void testGetById_PastReminder_ThrowsException() {
        Reminder reminder = new Reminder();
        reminder.setDateTime(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1)).thenReturn(Optional.of(reminder));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> reminderService.getById(1));
        assertEquals("El recordatorio ya venció y no se puede recuperar", exception.getMessage());
    }

}