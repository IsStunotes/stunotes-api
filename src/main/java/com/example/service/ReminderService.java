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
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ActivityRepository activityRepository;
    private final ReminderMapper reminderMapper;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public ReminderResponse create(ReminderRequest request) {

        if (request == null || request.dateTime() == null || request.titulo() == null || request.titulo().trim().isEmpty()){
            throw new IllegalArgumentException("Faltan completar datos obligatorios (título y fechaHora)");
        }

        if (request.dateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del recordatorio debe ser futura");
        }

        User currentUser = getAuthenticatedUser();

        Activity activity = null;
        if (request.activityId() != null) {
            activity = activityRepository.findById(request.activityId())
                    .orElseThrow(()-> new ResourceNotFoundException("Actividad no encontrada"));
        }

        Reminder reminder = reminderMapper.toEntity(request, activity, currentUser);
        return reminderMapper.toResponse(reminderRepository.save(reminder));
    }



    @Transactional(readOnly = true)
    public List<ReminderResponse> getAll() {
        User user = getAuthenticatedUser();
        List<Reminder> reminders = reminderRepository.findByUserId(user.getId());

        return reminders.stream()
                .map(reminderMapper::toResponse)
                .toList();
    }

    //Actualizacion de reminder
    @Transactional
    public ReminderResponse update (@NotNull Long id, @NotNull ReminderRequest request){
        if (request == null || request.dateTime() == null || request.titulo() == null || request.titulo().trim().isEmpty()){
            throw new IllegalArgumentException("Faltan completar datos obligatorios (título y fechaHora)");
        }

        Reminder existingReminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recordatorio no encontrado con el id: " + id));

        User user = getAuthenticatedUser();
        if (!existingReminder.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No tienes permisos para modificar este recordatorio");
        }

        if (existingReminder.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede editar un recordatorio después de su fecha programada");
        }

        Activity activity = null;
        if (request.activityId() != null) {
            activity = activityRepository.findById(request.activityId())
                    .orElseThrow(()-> new ResourceNotFoundException("Actividad no encontrada"));
        }

        Reminder updatedReminder = reminderMapper.toEntity(request, activity, user);
        updatedReminder.setId(existingReminder.getId());
        return reminderMapper.toResponse(reminderRepository.save(updatedReminder));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recordatorio no encontrado"));

        User user = getAuthenticatedUser();
        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No tienes permisos para eliminar este recordatorio");
        }

        if (reminder.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede eliminar un recordatorio después de su fecha programada");
        }

        reminderRepository.delete(reminder);
    }
}
