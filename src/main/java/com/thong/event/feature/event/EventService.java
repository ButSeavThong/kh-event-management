package com.thong.event.feature.event;

import com.thong.event.domain.Event;
import com.thong.event.domain.EventAttendance;
import com.thong.event.feature.event.dto.EventRequest;
import com.thong.event.feature.event.dto.EventResponse;
import com.thong.event.feature.event.dto.UpdateEventRequest;
import com.thong.event.utils.EventStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> findAllEvents();

    // public
    Page<EventResponse> findAllEventsPublic(EventStatus status, Pageable pageable);
    void deleteEventById(Long id);
    EventResponse updateEventById(Long id, UpdateEventRequest updateEventRequest);
    EventResponse findEventById(Long id);
    void updateEventStatus(Long id, EventStatus status);
    // Attendance section
    EventAttendance joinEvent(Long eventId, String userEmail) throws BadRequestException;
    void cancelEvent(Long eventId, String userEmail);

    List<EventResponse> getUserJoinedEvents(String userEmail);

    List<EventResponse> filterEvents(
            String khan,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String title);

    List<EventResponse> filterEventsByCategory(Long categoryId, String categoryName);
}
