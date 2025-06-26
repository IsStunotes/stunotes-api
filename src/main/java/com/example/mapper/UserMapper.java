package com.example.mapper;

import com.example.dto.response.AuthResponseDTO;
import com.example.dto.request.LoginDTO;
import com.example.dto.response.UserProfileDTO;
import com.example.dto.request.UserRegistrationDTO;
import com.example.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toUserEntity(UserRegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    public UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO =  modelMapper.map(user, UserProfileDTO.class);

        if(user.getStudent()!=null){
            userProfileDTO.setName(user.getStudent().getName());
            userProfileDTO.setLastName(user.getStudent().getLastName());
        }

        if(user.getTeacher()!=null){
            userProfileDTO.setName(user.getTeacher().getName());
            userProfileDTO.setLastName(user.getTeacher().getLastName());
        }

        return userProfileDTO;
    }

    public User toUserEntity(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }

    public AuthResponseDTO toAuthResponseDTO(User user, String token){
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);

        String name = (user.getStudent() != null) ? user.getStudent().getName()
                : (user.getTeacher() != null) ? user.getTeacher().getName()
                : "Admin";
        String lastName = (user.getStudent() != null) ? user.getStudent().getLastName()
                : (user.getTeacher() != null) ? user.getTeacher().getLastName()
                : "User";
        Integer id = user.getId();
        String email = user.getEmail();

        authResponseDTO.setId(id);
        authResponseDTO.setEmail(email);
        authResponseDTO.setName(name);
        authResponseDTO.setLastName(lastName);

        authResponseDTO.setRole(user.getRole().getName().name());

        return authResponseDTO;
    }


}