package com.thong.event.feature.admin.controller;

import com.thong.event.feature.event.EventService;
import com.thong.event.feature.event.dto.EventRequest;
import com.thong.event.feature.event.dto.EventResponse;
import com.thong.event.feature.event.dto.UpdateEventRequest;
import com.thong.event.utils.EventStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
public class AdminEventController { // ready
    private final EventService eventService;
    /**
     * ADMIN: Get all events (including DRAFT and CANCELLED)
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    /**
     * ADMIN: Get event by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }

    /**
     * ADMIN: Create a new event
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }
    
    /**
     * ADMIN: Update an existing event
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(eventService.updateEventById(id, request));
    }
    
    /**
     * ADMIN: Update event status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEventStatus(
            @PathVariable Long id,
            @RequestParam EventStatus status) {
        eventService.updateEventStatus(id, status);
    }
    
    /**
     * ADMIN: Delete an event
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }
}