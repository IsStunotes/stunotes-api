package com.example.service;
import com.example.dto.AuthResponseDTO;
import com.example.dto.LoginDTO;
import com.example.dto.UserProfileDTO;
import com.example.dto.UserRegistrationDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.RoleNotFoundException;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.repository.RoleRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import com.example.security.TokenProvider;
import com.example.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public UserProfileDTO registerStudent(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.STUDENT);
    }

    @Transactional
    @Override
    public UserProfileDTO registerTeacher(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.TEACHER);
    }

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        boolean isAdmin = user.getRole().getName().equals(ERole.ADMIN);

        String token = tokenProvider.createAccessToken(authentication);

        AuthResponseDTO responseDTO = userMapper.toAuthResponseDTO(user, token);

        return responseDTO;
    }


    @Transactional
    @Override
    public UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        boolean existsAsStudent = studentRepository.existsByNameAndLastNameAndUserIdNot(
                userProfileDTO.getName(), userProfileDTO.getLastName(), id);
        boolean existsAsTeacher = teacherRepository.existsByNameAndLastNameAndUserIdNot(
                userProfileDTO.getName(), userProfileDTO.getLastName(), id);

        if (existsAsStudent || existsAsTeacher) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }


        if (user.getStudent() != null) {

            user.getStudent().setName(userProfileDTO.getName());
            user.getStudent().setLastName(userProfileDTO.getLastName());
        }

        if (user.getTeacher() != null) {
            user.getTeacher().setName(userProfileDTO.getName());
            user.getTeacher().setLastName(userProfileDTO.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserProfileDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDTO getUserProfileById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return userMapper.toUserProfileDTO(user);
    }

    private UserProfileDTO registerUserWithRole(UserRegistrationDTO registrationDTO, ERole roleEnum) {

        boolean existsByEmail = userRepository.existsByEmail(registrationDTO.getEmail());
        boolean existsAsStudent = studentRepository.existsByNameAndLastName(registrationDTO.getName(), registrationDTO.getLastName());
        boolean existsAsTeacher = teacherRepository.existsByNameAndLastName(registrationDTO.getName(), registrationDTO.getLastName());

        if (existsByEmail) {
            throw new IllegalArgumentException("El email ya esta registrado");
        }

        if (existsAsStudent || existsAsTeacher) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));

        registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        User user = userMapper.toUserEntity(registrationDTO);
        user.setRole(role);

        if (roleEnum == ERole.STUDENT) {
            Student student = new Student();
            student.setName(registrationDTO.getName());
            student.setLastName(registrationDTO.getLastName());
            student.setCreatedAt(LocalDateTime.now());
            student.setUser(user);
            user.setStudent(student);
        } else if (roleEnum == ERole.TEACHER) {
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