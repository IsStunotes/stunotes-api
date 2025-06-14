package com.example.repository;


import com.example.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
    // Verificar si existe un recordatorio para una actividad en una fecha específica
    boolean existsByActivityIdAndDateTime(Integer activityId, LocalDateTime dateTime);

    // Buscar recordatorios por actividad
    List<Reminder> findByActivityId(Integer activityId);

    // Buscar recordatorios futuros
    @Query("SELECT r FROM Reminder r WHERE r.dateTime > :currentDateTime")
    List<Reminder> findFutureReminders(@Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT r FROM Reminder r WHERE r.dateTime BETWEEN :startDate AND :endDate")
    List<Reminder> findByDateTimeBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);


}
