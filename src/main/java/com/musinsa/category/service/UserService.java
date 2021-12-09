package com.musinsa.category.service;

import com.musinsa.category.dto.UserDto;
import com.musinsa.category.entity.User;
import com.musinsa.category.exception.DuplicateException;
import com.musinsa.category.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto signup(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateException(userDto.getEmail()+" is an already registered user.");
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        return UserDto.from(userRepository.save(user));
    }
}
