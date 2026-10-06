package com.jobtracker.service;

import com.jobtracker.dto.admin.AdminStatsResponse;
import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.User;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.InterviewRepository;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JobApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final UserMapper userMapper;

    public AdminService(UserRepository userRepository, JobApplicationRepository applicationRepository, InterviewRepository interviewRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> users() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public UserResponse user(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Transactional
    public UserResponse disable(Long id) {
        User user = findUser(id);
        user.setEnabled(false);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse enable(Long id) {
        User user = findUser(id);
        user.setEnabled(true);
        return userMapper.toResponse(userRepository.save(user));
    }

    public AdminStatsResponse stats() {
        return new AdminStatsResponse(
                userRepository.count(),
                userRepository.countByEnabledTrue(),
                applicationRepository.count(),
                applicationRepository.countByAppliedDate(LocalDate.now()),
                interviewRepository.count(),
                applicationRepository.countByStatus(ApplicationStatus.OFFER)
        );
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
