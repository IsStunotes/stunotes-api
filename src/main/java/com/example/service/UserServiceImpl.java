package com.example.service;
import com.example.dto.UserProfileDTO;
import com.example.dto.UserRegistrationDTO;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.repository.RoleRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserProfileDTO registerStudent(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.STUDENT);
    }

    @Override
    public UserProfileDTO registerTeacher(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.TEACHER);
    }

    @Override
    public UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO) {
        return null;
    }

    @Override
    public UserProfileDTO getUserProfileById(Integer id) {
        return null;
    }

    private UserProfileDTO registerUserWithRole(UserRegistrationDTO registrationDTO, ERole roleEnum) {
        boolean existsByEmail = userRepository.existsByEmail(registrationDTO.getEmail());
        boolean existsAsStudent = studentRepository.existsByNameAndLastName(registrationDTO.getName(), registrationDTO.getLastName());
        boolean existsAsTeacher = teacherRepository.existsByNameAndLastName(registrationDTO.getName(), registrationDTO.getLastName());

        if(existsByEmail){
            throw new IllegalArgumentException("Email already exists");
        }

        if(existsAsStudent || existsAsTeacher){
            throw new IllegalArgumentException("Ya existe usuario con el mismo nombre y apellido");
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        User user = userMapper.toUserEntity(registrationDTO);
        user.setRole(role);

        if(roleEnum == ERole.STUDENT){
            Student student = new Student();
            student.setName(registrationDTO.getName());
            student.setLastName(registrationDTO.getLastName());
            student.setCreatedAt(LocalDateTime.now());
            student.setUser(user);
            user.setStudent(student);
        }else if(roleEnum == ERole.TEACHER){
            Teacher teacher = new Teacher();
            teacher.setName(registrationDTO.getName());
            teacher.setLastName(registrationDTO.getLastName());
            teacher.setCreatedAt(LocalDateTime.now());
            teacher.setUser(user);
            user.setTeacher(teacher);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toUserProfileDTO(savedUser);
    }
}

