package com.mams.security;

import com.mams.entity.User;
import com.mams.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthHelper(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public User getCurrentUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return null;
            }
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
