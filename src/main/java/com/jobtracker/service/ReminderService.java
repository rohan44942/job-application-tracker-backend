package com.jobtracker.service;

import com.jobtracker.dto.reminder.ReminderRequest;
import com.jobtracker.dto.reminder.ReminderResponse;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.Reminder;
import com.jobtracker.entity.ReminderStatus;
import com.jobtracker.entity.User;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.ReminderRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;

    public ReminderService(ReminderRepository reminderRepository, ApplicationService applicationService, CurrentUserService currentUserService) {
        this.reminderRepository = reminderRepository;
        this.applicationService = applicationService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public ReminderResponse create(ReminderRequest request) {
        User user = currentUserService.getCurrentUser();
        Reminder reminder = new Reminder();
        reminder.setUser(user);
        if (request.applicationId() != null) {
            JobApplication application = applicationService.findOwned(request.applicationId());
            reminder.setApplication(application);
        }
        reminder.setTitle(request.title());
        reminder.setMessage(request.message());
        reminder.setRemindAt(request.remindAt());
        return toResponse(reminderRepository.save(reminder));
    }

    public List<ReminderResponse> getAll() {
        return reminderRepository.findByUserOrderByRemindAtAsc(currentUserService.getCurrentUser()).stream().map(this::toResponse).toList();
    }

    @Transactional
    public ReminderResponse cancel(Long id) {
        Reminder reminder = reminderRepository.findByIdAndUser(id, currentUserService.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        reminder.setStatus(ReminderStatus.CANCELLED);
        return toResponse(reminderRepository.save(reminder));
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void markDueRemindersSent() {
        List<Reminder> reminders = reminderRepository.findTop50ByStatusAndRemindAtLessThanEqualOrderByRemindAtAsc(ReminderStatus.PENDING, LocalDateTime.now());
        reminders.forEach(reminder -> reminder.setStatus(ReminderStatus.SENT));
        reminderRepository.saveAll(reminders);
    }

    private ReminderResponse toResponse(Reminder reminder) {
        Long applicationId = reminder.getApplication() == null ? null : reminder.getApplication().getId();
        return new ReminderResponse(reminder.getId(), applicationId, reminder.getTitle(), reminder.getMessage(), reminder.getRemindAt(), reminder.getStatus(), reminder.getCreatedAt());
    }
}
