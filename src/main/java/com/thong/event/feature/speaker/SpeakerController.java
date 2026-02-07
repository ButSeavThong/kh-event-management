package com.thong.event.feature.speaker;

import com.thong.event.feature.speaker.dto.SpeakerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    private final SpeakerService speakerService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpeakerResponse> getSpeakerById(@PathVariable Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }
}
