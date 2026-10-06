package com.jobtracker.repository;

import com.jobtracker.entity.Reminder;
import com.jobtracker.entity.ReminderStatus;
import com.jobtracker.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserOrderByRemindAtAsc(User user);
    Optional<Reminder> findByIdAndUser(Long id, User user);
    List<Reminder> findTop50ByStatusAndRemindAtLessThanEqualOrderByRemindAtAsc(ReminderStatus status, LocalDateTime remindAt);
}
