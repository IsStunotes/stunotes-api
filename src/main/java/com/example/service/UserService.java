package com.example.service;

import com.example.dto.AuthResponseDTO;
import com.example.dto.LoginDTO;
import com.example.dto.UserProfileDTO;
import com.example.dto.UserRegistrationDTO;

public interface UserService {
    UserProfileDTO registerStudent(UserRegistrationDTO registrationDTO);

    UserProfileDTO registerTeacher(UserRegistrationDTO registrationDTO);

    AuthResponseDTO login(LoginDTO loginDTO);

    UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO);

    UserProfileDTO getUserProfileById(Integer id);
}

