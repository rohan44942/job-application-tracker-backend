package com.jobtracker.service;

import com.jobtracker.dto.MessageResponse;
import com.jobtracker.dto.auth.LoginRequest;
import com.jobtracker.dto.auth.LoginResponse;
import com.jobtracker.dto.auth.RegisterRequest;
import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.entity.User;
import com.jobtracker.exception.DuplicateResourceException;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;
    private final AuditService auditService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, CurrentUserService currentUserService, UserMapper userMapper, AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.currentUserService = currentUserService;
        this.userMapper = userMapper;
        this.auditService = auditService;
    }

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        String email = request.email().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        auditService.record(user, "REGISTER", "User", user.getId(), "User registered");
        return new MessageResponse("User registered successfully");
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.email().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        User user = userRepository.findByEmail(email).orElseThrow();
        auditService.record(user, "LOGIN", "User", user.getId(), "User logged in");
        return new LoginResponse(jwtService.createToken(user), "Bearer", jwtService.expiresInSeconds());
    }

    public UserResponse me() {
        return userMapper.toResponse(currentUserService.getCurrentUser());
    }
}
