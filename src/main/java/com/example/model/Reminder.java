package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name="reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="DateTime",nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name="activity_id", referencedColumnName = "id",
    foreignKey = @ForeignKey(name="FK_reminder_activities"))
    private Activity activity;

}
