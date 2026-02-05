package com.thong.event.feature.baseController;

import com.thong.event.domain.Event;
import com.thong.event.domain.EventAttendance;
import com.thong.event.feature.event.EventService;
import com.thong.event.feature.event.EventServices;
import com.thong.event.feature.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/events")
@RequiredArgsConstructor
public class UserEventController {
    private final EventService eventService;
    /**
     * USER: Join an event
     * Requires USER or ADMIN role
     */
    @PostMapping("/{eventId}/join")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    public ResponseEntity<EventAttendance> joinEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal Jwt jwt) throws BadRequestException {
        String userEmail = jwt.getSubject();  // Extract email from JWT
        return ResponseEntity.ok(eventService.joinEvent(eventId, userEmail));
    }
    
    /**
     * USER: Cancel event attendance
     * Requires USER or ADMIN role
     */
    @PostMapping("/{eventId}/cancel")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    public ResponseEntity<Void> cancelEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal Jwt jwt) {
        
        String userEmail = jwt.getSubject();
        eventService.cancelEvent(eventId, userEmail);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * USER: Get my joined events
     * Requires USER or ADMIN role
     */
    @GetMapping("/my-events")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    public ResponseEntity<List<EventResponse>> getMyJoinedEvents(
            @AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getSubject();
        return ResponseEntity.ok(eventService.getUserJoinedEvents(userEmail));
    }
}