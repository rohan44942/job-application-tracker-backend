package com.jobtracker.service;

import com.jobtracker.dto.note.NoteRequest;
import com.jobtracker.dto.note.NoteResponse;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.Note;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.NoteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;
    private final AuditService auditService;

    public NoteService(NoteRepository noteRepository, ApplicationService applicationService, CurrentUserService currentUserService, AuditService auditService) {
        this.noteRepository = noteRepository;
        this.applicationService = applicationService;
        this.currentUserService = currentUserService;
        this.auditService = auditService;
    }

    @Transactional
    public NoteResponse create(Long applicationId, NoteRequest request) {
        JobApplication application = applicationService.findOwned(applicationId);
        Note note = new Note();
        note.setApplication(application);
        note.setTitle(request.title());
        note.setContent(request.content());
        Note saved = noteRepository.save(note);
        auditService.record(currentUserService.getCurrentUser(), "CREATE_NOTE", "Note", saved.getId(), application.getCompanyName());
        return toResponse(saved);
    }

    public List<NoteResponse> getByApplication(Long applicationId) {
        JobApplication application = applicationService.findOwned(applicationId);
        return noteRepository.findByApplicationOrderByCreatedAtDesc(application).stream().map(this::toResponse).toList();
    }

    @Transactional
    public NoteResponse update(Long id, NoteRequest request) {
        Note note = findOwned(id);
        note.setTitle(request.title());
        note.setContent(request.content());
        return toResponse(noteRepository.save(note));
    }

    @Transactional
    public void delete(Long id) {
        Note note = findOwned(id);
        noteRepository.delete(note);
        auditService.record(currentUserService.getCurrentUser(), "DELETE_NOTE", "Note", id, null);
    }

    private Note findOwned(Long id) {
        Long userId = currentUserService.getCurrentUser().getId();
        return noteRepository.findByIdAndApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(note.getId(), note.getApplication().getId(), note.getTitle(), note.getContent(), note.getCreatedAt(), note.getUpdatedAt());
    }
}
