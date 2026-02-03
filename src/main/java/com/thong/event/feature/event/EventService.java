package com.thong.event.feature.event;

import com.thong.event.domain.*;
import com.thong.event.exception.ResourceNotFoundException;
import com.thong.event.feature.category.CategoryRepository;
import com.thong.event.feature.event.dto.EventRequest;
import com.thong.event.feature.eventAttendance.EventAttendanceRepository;
import com.thong.event.feature.user.UserRespository;
import com.thong.event.utils.AttendanceStatus;
import com.thong.event.utils.EventStatus;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final EventAttendanceRepository attendanceRepository;
    private final CategoryRepository categoryRepository;
    private final UserRespository userRespository;
    
    // ============= PUBLIC METHODS =============
    
    /**
     * Get all published events (accessible to everyone)
     */
    public List<Event> getAllPublishedEvents() {
        return eventRepository.findByStatus(EventStatus.PUBLISHED);
    }
    
    /**
     * Get event by ID
     */
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Event not found with id: " + id));
    }
    
    /**
     * Filter events by various criteria
     */
    public List<Event> filterEvents(
            String khan, 
            Long categoryId,
            LocalDate startDate, 
            LocalDate endDate,
            String title) {
        
        if (khan != null) {
            return eventRepository.findByStatusAndKhan(
                EventStatus.PUBLISHED, khan);
        }
        
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));
            return eventRepository.findByStatusAndCategory(
                EventStatus.PUBLISHED, category);
        }
        
        if (startDate != null && endDate != null) {
            return eventRepository.findByStatusAndStartDateBetween(
                EventStatus.PUBLISHED, startDate, endDate);
        }
        
        if (title != null && !title.isBlank()) {
            return eventRepository.findByStatusAndTitleContainingIgnoreCase(
                EventStatus.PUBLISHED, title);
        }
        
        return getAllPublishedEvents();
    }
    
    // ============= USER METHODS =============
    
    /**
     * User joins an event
     * Business rules:
     * - User must not have already joined
     * - Event must not be full
     */
    @Transactional
    public EventAttendance joinEvent(Long eventId, String userEmail) throws BadRequestException {
        Event event = getEventById(eventId);
        User user = userRespository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userEmail));

        // Check if event is published
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BadRequestException("Event is not available for registration");
        }
        
        // Check if already joined
        Optional<EventAttendance> existing = 
            attendanceRepository.findByUserAndEvent(user, event);
        
        if (existing.isPresent() && 
            existing.get().getStatus() == AttendanceStatus.JOINED) {
            throw new BadRequestException(
                "You have already joined this event");
        }
        
        // Check capacity
        long currentAttendees = attendanceRepository.countByEventAndStatus(
            event, AttendanceStatus.JOINED);
        
        if (currentAttendees >= event.getCapacity()) {
            throw new BadRequestException(
                "Event is full. Capacity: " + event.getCapacity());
        }
        
        // Create or update attendance
        EventAttendance attendance = existing.orElse(
            EventAttendance.builder()
                .user(user)
                .event(event)
                .build()
        );
        attendance.setStatus(AttendanceStatus.JOINED);
        
        return attendanceRepository.save(attendance);
    }
    
    /**
     * User cancels their event attendance
     */
    @Transactional
    public void cancelEvent(Long eventId, String userEmail) {
        Event event = getEventById(eventId);
        User user = userRespository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userEmail));

        EventAttendance attendance = attendanceRepository
            .findByUserAndEvent(user, event)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Event You have not joined yet "
                ));
        
        if (attendance.getStatus() == AttendanceStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event has been cancelled");
        }
        
        attendance.setStatus(AttendanceStatus.CANCELLED);
        attendanceRepository.save(attendance);
    }
    
    /**
     * Get events joined by a user
     */
    public List<Event> getUserJoinedEvents(String userEmail) {
        User user = userRespository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userEmail));

        return attendanceRepository
            .findByUserAndStatus(user, AttendanceStatus.JOINED)
            .stream()
            .map(EventAttendance::getEvent)
            .collect(Collectors.toList());
    }
    
    // ============= ADMIN METHODS =============
    
    /**
     * ADMIN: Create a new event
     */
    @Transactional
    public Event createEvent(EventRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Category not found with id: " + request.getCategoryId()));
        
        Event event = Event.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .location(request.getLocation())
            .khan(request.getKhan())
            .price(request.getPrice())
            .capacity(request.getCapacity())
            .status(EventStatus.DRAFT)  // New events start as DRAFT
            .category(category)
            .imageUrls(request.getImageUrls())
            .build();
        
        return eventRepository.save(event);
    }
    
    /**
     * ADMIN: Update an existing event
     */
    @Transactional
    public Event updateEvent(Long id, EventRequest request) {
        Event event = getEventById(id);
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Category not found with id: " + request.getCategoryId()));
        
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        event.setKhan(request.getKhan());
        event.setPrice(request.getPrice());
        event.setCapacity(request.getCapacity());
        event.setCategory(category);
        event.setImageUrls(request.getImageUrls());
        
        return eventRepository.save(event);
    }
    
    /**
     * ADMIN: Update event status (DRAFT → PUBLISHED → CANCELLED)
     */
    @Transactional
    public Event updateEventStatus(Long id, EventStatus status) {
        Event event = getEventById(id);
        event.setStatus(status);
        return eventRepository.save(event);
    }
    
    /**
     * ADMIN: Delete an event
     */
    @Transactional
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }
    
    /**
     * ADMIN: Get all events (including DRAFT and CANCELLED)
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}