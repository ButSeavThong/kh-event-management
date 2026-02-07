package com.thong.event.feature.admin.controller;

import com.thong.event.domain.Speaker;
import com.thong.event.feature.speaker.SpeakerMapper;
import com.thong.event.feature.speaker.SpeakerService;
import com.thong.event.feature.speaker.dto.SpeakerRequest;
import com.thong.event.feature.speaker.dto.SpeakerResponse;
import com.thong.event.feature.speaker.dto.UpdateSpeakerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/speakers")
@RequiredArgsConstructor
public class AdminSpeakerController {
    
    private final SpeakerService speakerService;
    private final SpeakerMapper speakerMapper;
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<SpeakerResponse>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<SpeakerResponse>> searchSpeakers(
            @RequestParam String name) {
        return ResponseEntity.ok(speakerService.searchSpeakers(name));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public SpeakerResponse createSpeaker(
            @Valid @RequestBody SpeakerRequest request) {
        return speakerService.createSpeaker(request);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<SpeakerResponse> updateSpeaker(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpeakerRequest request) {
        return ResponseEntity.ok(speakerService.updateSpeaker(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SpeakerResponse> getSpeakerById(@PathVariable Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }
}