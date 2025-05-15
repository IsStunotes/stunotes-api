package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sectionId;

    @Column(name = "name_section", nullable = false, length = 50)
    private String name;

    @Column(name = "description_section", length = 200)
    private String description;

    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "FK_teacher_section"))
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "FK_student_section"))
    private Student student;
}

