package com.jobtracker.repository;

import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.Note;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByApplicationOrderByCreatedAtDesc(JobApplication application);
    Optional<Note> findByIdAndApplicationUserId(Long id, Long userId);
}
