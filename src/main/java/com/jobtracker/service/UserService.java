package com.jobtracker.service;

import com.jobtracker.dto.MessageResponse;
import com.jobtracker.dto.user.ChangePasswordRequest;
import com.jobtracker.dto.user.UpdateProfileRequest;
import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.entity.User;
import com.jobtracker.exception.BadRequestException;
import com.jobtracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(CurrentUserService currentUserService, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse profile() {
        return userMapper.toResponse(currentUserService.getCurrentUser());
    }

    @Transactional
    public UserResponse update(UpdateProfileRequest request) {
        User user = currentUserService.getCurrentUser();
        user.setName(request.name());
        user.setPhone(request.phone());
        user.setLocation(request.location());
        user.setYearsOfExperience(request.yearsOfExperience());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest request) {
        User user = currentUserService.getCurrentUser();
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return new MessageResponse("Password changed successfully");
    }
}
