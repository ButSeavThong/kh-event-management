package com.thong.event.feature.eventSpeaker;

import com.thong.event.domain.EventSpeaker;
import com.thong.event.feature.eventSpeaker.dto.AddSpeakerRequest;
import com.thong.event.feature.eventSpeaker.dto.EventSpeakerResponse;
import com.thong.event.feature.eventSpeaker.dto.ReorderSpeakersRequest;
import com.thong.event.feature.eventSpeaker.dto.UpdateSpeakerInEventRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventSpeakerController {
    private final EventSpeakerService eventSpeakerService;

    // ============= PUBLIC ENDPOINTS =============

    /**
     * PUBLIC: Get all speakers for an event done!
     * No authentication required
     */
    @GetMapping("/events/{eventId}/speakers")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventSpeakerResponse>> getEventSpeakers(
            @PathVariable Long eventId) {
        List<EventSpeakerResponse> speakers = 
            eventSpeakerService.getEventSpeakers(eventId);
        return ResponseEntity.ok(speakers);
    }
    
    /**
     * PUBLIC: Get all events for a speaker
     * No authentication required
     */
//    @GetMapping("/speakers/{speakerId}/events")
//    @PreAuthorize("permitAll()")
//    public ResponseEntity<List<Event>> getSpeakerEvents(
//            @PathVariable Long speakerId) {
//
//        List<Event> events = eventSpeakerService.getSpeakerEvents(speakerId);
//        return ResponseEntity.ok(events);
//    }
    
    // ============= ADMIN ENDPOINTS =============
    
    /**
     * ADMIN: Add a speaker to an event done !
     */
    @PostMapping("/admin/events/{eventId}/speakers")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public void addSpeakerToEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody AddSpeakerRequest request) {
        eventSpeakerService.addSpeakerToEvent(eventId, request);
    }
    
    /**
     * ADMIN: Remove a speaker from an event done !
     */
    @DeleteMapping("/admin/events/{eventId}/speakers/{speakerId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> removeSpeakerFromEvent(
            @PathVariable Long eventId,
            @PathVariable Long speakerId) {
        eventSpeakerService.removeSpeakerFromEvent(eventId, speakerId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * ADMIN: Update speaker metadata (order, role, time, fee) done !
     */
    @PutMapping("/admin/events/{eventId}/speakers/{speakerId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<EventSpeakerResponse> updateEventSpeaker(
            @PathVariable Long eventId,
            @PathVariable Long speakerId,
            @Valid @RequestBody UpdateSpeakerInEventRequest request) {
        EventSpeaker eventSpeaker = eventSpeakerService.updateEventSpeaker(
            eventId, speakerId, request);
        return ResponseEntity.ok(toResponse(eventSpeaker));
    }
    
    /**
     * ADMIN: Reorder speakers for an event
     */
    @PutMapping("/admin/events/{eventId}/speakers/reorder")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> reorderSpeakers(
            @PathVariable Long eventId,
            @Valid @RequestBody ReorderSpeakersRequest request) {
        
        eventSpeakerService.reorderSpeakers(eventId, request.getSpeakerIds());
        return ResponseEntity.ok().build();
    }

    // Helper method to convert to DTO
    private EventSpeakerResponse toResponse(EventSpeaker eventSpeaker) {
        return EventSpeakerResponse.builder()
                .id(eventSpeaker.getId())
                .speakerId(eventSpeaker.getSpeaker().getId())
                .title(eventSpeaker.getSpeaker().getTitle())
                .company(eventSpeaker.getSpeaker().getCompany())
                .fullName(eventSpeaker.getSpeaker().getFullName())
                .imageUrl(eventSpeaker.getSpeaker().getImageUrl())
                .displayOrder(eventSpeaker.getDisplayOrder())
                .role(eventSpeaker.getRole())
                .speakingTime(eventSpeaker.getSpeakingTime())
                .speakerFee(eventSpeaker.getSpeakerFee())
                .build();
    }
}