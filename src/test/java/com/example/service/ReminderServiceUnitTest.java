package com.example.service;

import com.example.dto.request.ReminderRequest;
import com.example.dto.response.ReminderResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ReminderMapper;
import com.example.model.Activity;
import com.example.model.Reminder;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.ReminderRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReminderServiceUnitTest {

    @Mock private ReminderRepository reminderRepository;
    @Mock private ActivityRepository activityRepository;
    @Mock private ReminderMapper reminderMapper;
    @Mock private UserRepository userRepository;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;
    @InjectMocks private ReminderService reminderService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test user
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");

        // Mock security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    // Helper method to create a reminder with the test user
    private Reminder createReminderWithUser(LocalDateTime dateTime) {
        Reminder reminder = new Reminder();
        reminder.setUser(testUser);
        reminder.setDateTime(dateTime);
        return reminder;
    }

    // US07 - Crear recordatorios

    @Test
    @DisplayName("CP35 - Crear recordatorio válido")
    void testCreateReminder_Success() {
        ReminderRequest request = new ReminderRequest(1L, "Test Reminder", 100, LocalDateTime.now().plusDays(1));
        Activity activity = new Activity();
        Reminder reminder = new Reminder();
        ReminderResponse response = new ReminderResponse(1L, "Test Reminder", "Activity Name", request.dateTime(), 100);

        when(activityRepository.findById(100)).thenReturn(Optional.of(activity));
        when(reminderMapper.toEntity(request, activity, testUser)).thenReturn(reminder);
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
        ReminderRequest request = new ReminderRequest(1L, null, null, null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });
        assertEquals("Faltan completar datos obligatorios (título y fechaHora)", exception.getMessage());
    }

    @Test
    @DisplayName("CP37 - Crear recordatorio con fecha pasada")
    void testCreateReminder_PastDate_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "Test Reminder", 100, LocalDateTime.now().minusDays(1));
        Activity activity = new Activity();
        when(activityRepository.findById(100)).thenReturn(Optional.of(activity));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("La fecha del recordatorio debe ser futura", exception.getMessage());
    }

    @Test
    @DisplayName("CP38 - Crear recordatorio sin actividad")
    void testCreateReminder_NoActivityId_Success() {
        ReminderRequest request = new ReminderRequest(1L, "Test Reminder", null, LocalDateTime.now().plusDays(1));
        Reminder reminder = new Reminder();
        ReminderResponse response = new ReminderResponse(1L, "Test Reminder", null, request.dateTime(), null);

        when(reminderMapper.toEntity(request, null, testUser)).thenReturn(reminder);
        when(reminderRepository.save(reminder)).thenReturn(reminder);
        when(reminderMapper.toResponse(reminder)).thenReturn(response);

        ReminderResponse result = reminderService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(reminderRepository).save(reminder);
    }

    @Test
    @DisplayName("CP39 - Crear recordatorio con actividad no encontrada")
    void testCreateReminder_ActivityNotFound_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "Test Reminder", 999, LocalDateTime.now().plusDays(1));
        when(activityRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("Actividad no encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("CP40 - Crear recordatorio con título vacío")
    void testCreateReminder_EmptyTitle_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "", 100, LocalDateTime.now().plusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.create(request);
        });

        assertEquals("Faltan completar datos obligatorios (título y fechaHora)", exception.getMessage());
    }



    // US12 - Editar y eliminar recordatorios

    @Test
    @DisplayName("CP70 - Editar recordatorio creado valido")
    void testUpdateReminder_Success() {
        ReminderRequest request = new ReminderRequest(1L, "Updated Reminder", 100, LocalDateTime.now().plusDays(1));
        Activity activity = new Activity();
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().plusDays(2));
        existingReminder.setUser(testUser); // Agregar el usuario al recordatorio existente
        Reminder updatedReminder = new Reminder();
        ReminderResponse response = new ReminderResponse(1L, "Updated Reminder", "Activity Name", request.dateTime(), 100);

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(existingReminder));
        when(activityRepository.findById(100)).thenReturn(Optional.of(activity));
        when(reminderMapper.toEntity(request, activity, testUser)).thenReturn(updatedReminder);
        when(reminderRepository.save(updatedReminder)).thenReturn(updatedReminder);
        when(reminderMapper.toResponse(updatedReminder)).thenReturn(response);

        ReminderResponse result = reminderService.update(1L, request);

        assertNotNull(result);
        verify(reminderRepository).save(updatedReminder);
    }

    @Test
    @DisplayName("CP71 - Editar recordatorio creado invalido (datos faltantes)")
    void testUpdateReminder_MissingData_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, null, 100, LocalDateTime.now().plusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.update(1L, request);
        });

        assertEquals("Faltan completar datos obligatorios (título y fechaHora)", exception.getMessage());
    }

    @Test
    @DisplayName("CP71b - Editar recordatorio con fecha pasada")
    void testUpdateReminder_PastDate_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "Updated Reminder", 100, LocalDateTime.now().plusDays(1));
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().minusDays(1));
        existingReminder.setUser(testUser); // Agregar el usuario al recordatorio existente

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(existingReminder));

        Exception exception = assertThrows(IllegalStateException.class, () -> reminderService.update(1L, request));
        assertEquals("No se puede editar un recordatorio después de su fecha programada", exception.getMessage());
    }

    @Test
    @DisplayName("CP72 - Verificar que no se pueden editar recordatorios de otros usuarios")
    void testUpdateReminder_UnauthorizedUser_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "Updated Reminder", 100, LocalDateTime.now().plusDays(1));
        Reminder existingReminder = new Reminder();
        existingReminder.setDateTime(LocalDateTime.now().plusDays(2));
        User otherUser = new User();
        otherUser.setId(999); // Usuario diferente
        existingReminder.setUser(otherUser);

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(existingReminder));

        Exception exception = assertThrows(SecurityException.class, () -> reminderService.update(1L, request));
        assertEquals("No tienes permisos para modificar este recordatorio", exception.getMessage());
    }

    @Test
    @DisplayName("CP73 - Editar recordatorio inexistente")
    void testUpdateReminder_NotFound_ThrowsException() {
        ReminderRequest request = new ReminderRequest(1L, "Updated Reminder", 100, LocalDateTime.now().plusDays(1));
        when(reminderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> reminderService.update(1L, request));
        assertEquals("Recordatorio no encontrado con el id: 1", exception.getMessage());
    }

    @Test
    @DisplayName("CP74 - Eliminar recordatorio")
    void testDeleteReminder_Success() {
        Reminder reminder = createReminderWithUser(LocalDateTime.now().plusDays(1));

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(reminder));

        reminderService.delete(1L);

        verify(reminderRepository).delete(reminder);
    }

    @Test
    @DisplayName("CP75 - Eliminar recordatorio invalido")
    void testDeleteReminder_PastDate_ThrowsException() {
        Reminder reminder = createReminderWithUser(LocalDateTime.now().minusDays(1));

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(reminder));

        Exception exception = assertThrows(IllegalStateException.class, () -> reminderService.delete(1L));
        assertEquals("No se puede eliminar un recordatorio después de su fecha programada", exception.getMessage());
    }

    @Test
    @DisplayName("CP76 - Verificar que no se pueden eliminar recordatorios de otros usuarios")
    void testDeleteReminder_UnauthorizedUser_ThrowsException() {
        Reminder reminder = createReminderWithUser(LocalDateTime.now().plusDays(1));
        User otherUser = new User();
        otherUser.setId(999); // Usuario diferente
        reminder.setUser(otherUser);

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(reminder));

        Exception exception = assertThrows(SecurityException.class, () -> reminderService.delete(1L));
        assertEquals("No tienes permisos para eliminar este recordatorio", exception.getMessage());
    }

    @Test
    @DisplayName("CP77 - Eliminar recordatorio que no existe")
    void testDeleteReminder_NotFound_ThrowsException() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> reminderService.delete(1L));
        assertEquals("Recordatorio no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP78 - Obtener todos los recordatorios")
    void testGetAll_Success() {
        List<Reminder> reminders = List.of(new Reminder(), new Reminder());
        List<ReminderResponse> responses = List.of(
                new ReminderResponse(1L, "Reminder 1", "Activity 1", LocalDateTime.now().plusDays(1), 1),
                new ReminderResponse(2L, "Reminder 2", "Activity 2", LocalDateTime.now().plusDays(2), 2)
        );

        when(reminderRepository.findByUserId(testUser.getId())).thenReturn(reminders);
        when(reminderMapper.toResponse(reminders.get(0))).thenReturn(responses.get(0));
        when(reminderMapper.toResponse(reminders.get(1))).thenReturn(responses.get(1));

        List<ReminderResponse> result = reminderService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reminderRepository).findByUserId(testUser.getId());
    }

}