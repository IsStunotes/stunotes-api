package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name="reminder")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="titulo", nullable = false)
    private String titulo;

    @Column(name="DateTime",nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name="activity_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name="FK_reminder_activities"))
    private Activity activity;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name="FK_reminder_user"))
    private User user;

}
