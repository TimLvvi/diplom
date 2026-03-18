package ru.skypro.homework.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthService(UserDetailsService userDetailsService, PasswordEncoder encoder, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    //Авторизация пользователя
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            return encoder.matches(password, userDetails.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    //Регистрация пользователя
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setEmail(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());

        userRepository.save(user);

        return true;
    }

}
