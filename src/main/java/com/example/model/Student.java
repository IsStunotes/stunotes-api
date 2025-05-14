package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "student")
public class Student extends User{

}
