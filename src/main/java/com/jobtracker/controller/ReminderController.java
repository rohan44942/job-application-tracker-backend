package com.jobtracker.controller;

import com.jobtracker.dto.reminder.ReminderRequest;
import com.jobtracker.dto.reminder.ReminderResponse;
import com.jobtracker.service.ReminderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@Valid @RequestBody ReminderRequest request) {
        return reminderService.create(request);
    }

    @GetMapping
    public List<ReminderResponse> getAll() {
        return reminderService.getAll();
    }

    @PatchMapping("/{id}/cancel")
    public ReminderResponse cancel(@PathVariable Long id) {
        return reminderService.cancel(id);
    }
}
