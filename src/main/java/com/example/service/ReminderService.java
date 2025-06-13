package com.example.service;


import com.example.dto.request.ReminderRequest;
import com.example.dto.response.ReminderResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ReminderMapper;
import com.example.model.Activity;
import com.example.model.Reminder;
import com.example.repository.ActivityRespository;
import com.example.repository.ReminderRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ActivityRespository activityRespository;
    private final ReminderMapper reminderMapper;


    //Creación de Reminder
    @Transactional
    public ReminderResponse create(ReminderRequest request) {


        //Validacion de datos nulos
        if (request == null || request.dateTime() == null || request.activityId() == null){
            throw new IllegalArgumentException("Faltan completar datos");
        }

        if (request.dateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del recordatorio debe ser futura");
        }
        //Validacion de existencia de actividad
        Activity activity = activityRespository.findById(request.activityId())
                .orElseThrow(()-> new ResourceNotFoundException("Actividad no encontrada"));

        Reminder reminder = reminderMapper.toEntity(request, activity);
        return reminderMapper.toResponse(reminderRepository.save(reminder));
    }



    //Actualizacion de reminder
    @Transactional
    public ReminderResponse update (@NotNull Integer id, @NotNull ReminderRequest request){
        // Validacion de datos nulos
        if (request == null || request.dateTime() == null || request.activityId() == null){
            throw new IllegalArgumentException("Falta completar datos");
        }

        // Busca el recordatorio existente
        Reminder existingReminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recordatorio no encontrado con el id: " + id));

        // Validar que la fecha no haya pasado
        if (existingReminder.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede editar un recordatorio después de su fecha programada");
        }

        //Valida que la actividad exista
        Activity activity = activityRespository.findById(request.activityId())
                .orElseThrow(()-> new ResourceNotFoundException("Actividad no encontrada"));

        Reminder updatedReminder = reminderMapper.toEntity(request, activity);
        updatedReminder.setId(existingReminder.getId());
        return reminderMapper.toResponse(reminderRepository.save(updatedReminder));
    }


    //Eliiniar Reminder
    @Transactional
    public void delete(@NotNull Integer id){
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Recordatorio no encontrado"));

        //validar que la fecha no haya pasado
        if (reminder.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede eliminar un recordatorio después de su fecha programada");
        }

        reminderRepository.delete(reminder);
    }

    //Eliminación recordatorio después de fecha
    @Transactional(readOnly = true)
    public ReminderResponse getById(@NotNull Integer id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recordatorio no encontrado"));

        if (reminder.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El recordatorio ya venció y no se puede recuperar");
        }

        return reminderMapper.toResponse(reminder);
    }




    //Implementaciones para export
    @Transactional(readOnly = true)
    public List<ReminderResponse> getRemindersForWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6); // semana desde el lunes (inclusive) hasta domingo

        List<Reminder> reminders = reminderRepository.findByDateTimeBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59));

        return reminders.stream()
                .map(reminderMapper::toResponse)
                .toList();
    }

}
