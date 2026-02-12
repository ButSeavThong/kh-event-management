package com.thong.event.feature.event;
import com.thong.event.feature.event.dto.EventResponse;
import com.thong.event.utils.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class PublicEventController {

    private  final EventService eventService;
    /**
     * PUBLIC: Get all published events
     * No authentication required
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<EventResponse>> getAllPublishedEvents( @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                eventService.findAllEventsPublic(EventStatus.PUBLISHED, pageable)
        );
    }
    
    /**
     * PUBLIC: Get event by ID
     * No authentication required
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }
    
    /**
     * PUBLIC: Filter events
     * No authentication required
     * Query params: khan, categoryId, startDate, endDate, title
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EventResponse>> filterEvents(
            @RequestParam(required = false) String khan,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(eventService.filterEvents(khan, categoryId, startDate, endDate, title));
    }



    /**
     * Public endpoint
     * - If categoryId is provided → filter by ID
     * - If categoryName is provided → filter by name
     * - If both are null → return all events
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    public  List<EventResponse> filterEventsByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName
    ) {
        return eventService.filterEventsByCategory(categoryId, categoryName);
    }
}
