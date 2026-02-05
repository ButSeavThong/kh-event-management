package com.thong.event.feature.admin.controller;

import com.thong.event.domain.Speaker;
import com.thong.event.feature.speaker.SpeakerService;
import com.thong.event.feature.speaker.dto.SpeakerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/speakers")
@RequiredArgsConstructor
public class AdminSpeakerController {
    
    private final SpeakerService speakerService;
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Speaker>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Speaker>> searchSpeakers(
            @RequestParam String name) {
        return ResponseEntity.ok(speakerService.searchSpeakers(name));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Speaker> createSpeaker(
            @Valid @RequestBody SpeakerRequest request) {
        Speaker speaker = speakerService.createSpeaker(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(speaker);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Speaker> updateSpeaker(
            @PathVariable Long id,
            @Valid @RequestBody SpeakerRequest request) {
        Speaker speaker = speakerService.updateSpeaker(id, request);
        return ResponseEntity.ok(speaker);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Speaker> getSpeakerById(@PathVariable Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }
}