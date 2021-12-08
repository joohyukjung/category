package com.musinsa.category.service;

import com.musinsa.category.dto.UserDto;
import com.musinsa.category.entity.User;
import com.musinsa.category.exception.DuplicateException;
import com.musinsa.category.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto signup(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateException("이미 가입되어 있는 사용자 입니다.("+userDto.getEmail()+")");
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        return UserDto.from(userRepository.save(user));
    }
}
