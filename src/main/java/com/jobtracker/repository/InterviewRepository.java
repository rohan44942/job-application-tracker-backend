package com.jobtracker.repository;

import com.jobtracker.entity.Interview;
import com.jobtracker.entity.JobApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationOrderByScheduledAtAsc(JobApplication application);
    Optional<Interview> findByIdAndApplicationUserId(Long id, Long userId);
    long countByApplicationUserId(Long userId);
}
