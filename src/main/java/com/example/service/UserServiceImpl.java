package com.example.service;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Usuario ya existe");
        }
        user.setCreatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User loginUser(User user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
    }
}

