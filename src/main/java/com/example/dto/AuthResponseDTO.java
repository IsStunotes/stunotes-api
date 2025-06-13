package com.example.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private Integer id;
    private String email;
    private String token;
    private String name;
    private String lastName;
    private String role;
}
