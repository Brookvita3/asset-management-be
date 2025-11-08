package com.example.demo.service;

import com.example.demo.config.jwt.JwtProvider;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFound("User not found with email: " + request.getEmail()));

        if (!Objects.equals(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid username or password");
        }

        if (!user.getActive()) {
            throw new AuthException("User account is inactive");
        }

        String token = jwtProvider.generateToken(user);

        return new LoginResponse(token);
    }
}
