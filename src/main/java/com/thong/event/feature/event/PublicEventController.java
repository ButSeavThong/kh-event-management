package com.thong.event.feature.event;
import com.thong.event.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class PublicEventController {
    
    private final EventService eventService;
    
    /**
     * PUBLIC: Get all published events
     * No authentication required
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Event>> getAllPublishedEvents() {
        List<Event> events = eventService.getAllPublishedEvents();
        return ResponseEntity.ok(events);
    }
    
    /**
     * PUBLIC: Get event by ID
     * No authentication required
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }
    
    /**
     * PUBLIC: Filter events
     * No authentication required
     * Query params: khan, categoryId, startDate, endDate, title
     */
    @GetMapping("/search")
    public ResponseEntity<List<Event>> filterEvents(
            @RequestParam(required = false) String khan,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String title) {
        
        List<Event> events = eventService.filterEvents(
            khan, categoryId, startDate, endDate, title);
        return ResponseEntity.ok(events);
    }
}