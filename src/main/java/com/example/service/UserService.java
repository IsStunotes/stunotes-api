package com.example.service;

import com.example.dto.response.AuthResponseDTO;
import com.example.dto.request.LoginDTO;
import com.example.dto.response.UserProfileDTO;
import com.example.dto.request.UserRegistrationDTO;

public interface UserService {
    UserProfileDTO registerStudent(UserRegistrationDTO registrationDTO);

    UserProfileDTO registerTeacher(UserRegistrationDTO registrationDTO);

    AuthResponseDTO login(LoginDTO loginDTO);

    UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO);

    UserProfileDTO getUserProfileById(Integer id);
}

