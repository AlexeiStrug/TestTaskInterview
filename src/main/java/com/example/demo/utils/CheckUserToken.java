package com.example.demo.utils;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckUserToken {

    private final UserRepository userRepository;

    public User loadUserByToken(String token) {
        token = token.replace("Bearer ", "");

        Optional<User> user = userRepository.findByToken(token);
        if (user.isEmpty()) throw new NotFoundException("User not found");

        return user.get();
    }
}
