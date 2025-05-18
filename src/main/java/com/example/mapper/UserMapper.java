package com.example.mapper;

import com.example.dto.UserProfileDTO;
import com.example.dto.UserRegistrationDTO;
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

        if(user.getTeacher()!=null){
            userProfileDTO.setName(user.getTeacher().getName());
            userProfileDTO.setLastName(user.getTeacher().getLastName());
        }

        if(user.getStudent()!=null){
            userProfileDTO.setName(user.getStudent().getName());
            userProfileDTO.setLastName(user.getStudent().getLastName());
        }

        return userProfileDTO;
    }
}