package com.jobtracker.controller;

import com.jobtracker.dto.note.NoteRequest;
import com.jobtracker.dto.note.NoteResponse;
import com.jobtracker.service.NoteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/applications/{applicationId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@PathVariable Long applicationId, @Valid @RequestBody NoteRequest request) {
        return noteService.create(applicationId, request);
    }

    @GetMapping("/applications/{applicationId}/notes")
    public List<NoteResponse> getByApplication(@PathVariable Long applicationId) {
        return noteService.getByApplication(applicationId);
    }

    @PutMapping("/notes/{id}")
    public NoteResponse update(@PathVariable Long id, @Valid @RequestBody NoteRequest request) {
        return noteService.update(id, request);
    }

    @DeleteMapping("/notes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        noteService.delete(id);
    }
}
