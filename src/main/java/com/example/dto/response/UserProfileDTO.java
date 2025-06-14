package com.example.dto.response;

import com.example.model.ERole;
import lombok.Data;

@Data
public class UserProfileDTO {

    private Integer id;
    private String email;
    private ERole role;
    private String name;
    private String lastName;


}
