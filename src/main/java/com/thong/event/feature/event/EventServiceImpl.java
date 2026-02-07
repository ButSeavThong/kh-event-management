package com.thong.event.feature.event;

import com.thong.event.domain.Category;
import com.thong.event.domain.Event;
import com.thong.event.domain.EventAttendance;
import com.thong.event.domain.User;
import com.thong.event.feature.category.CategoryRepository;
import com.thong.event.feature.event.dto.EventRequest;
import com.thong.event.feature.event.dto.EventResponse;
import com.thong.event.feature.event.dto.UpdateEventRequest;
import com.thong.event.feature.eventAttendance.EventAttendanceRepository;
import com.thong.event.feature.user.UserRespository;
import com.thong.event.utils.AttendanceStatus;
import com.thong.event.utils.EventStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private  final UserRespository userRespository;
    private final EventAttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public EventResponse createEvent(EventRequest eventRequest) {
        Category category = categoryRepository.findById(eventRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        Event event = eventMapper.toEvent(eventRequest);
        event.setCategory(category);
        event.setStatus(EventStatus.PUBLISHED);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventResponse(savedEvent);
    }

    // for admin
    @Override
    public List<EventResponse> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toEventResponseList(events);
    }

    @Override
    public List<EventResponse> findAllEventsPublic(EventStatus status) {
        List<Event> events = eventRepository.findByStatus(EventStatus.PUBLISHED);
        return eventMapper.toEventResponseList(events);
    }

    @Transactional
    @Override
    public void deleteEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        eventRepository.delete(event);
    }

    @Transactional
    @Override
    public EventResponse updateEventById(Long id, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        eventMapper.toEventPartially(updateEventRequest, event);
        event.setUpdatedAt(LocalDateTime.now());
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse findEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        return eventMapper.toEventResponse(event);
    }

    @Override
    @Transactional
    public void updateEventStatus(Long id, EventStatus status) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        event.setStatus(status);
        eventRepository.save(event);
    }


    @Transactional
    @Override
    public EventAttendance joinEvent(Long eventId, String userEmail) throws BadRequestException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event already joined");
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


    @Transactional
    public void cancelEvent(Long eventId, String userEmail) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
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


    // get Event that use joined
    // Get events joined by a user
    @Override
    public List<EventResponse> getUserJoinedEvents(String userEmail) {
        List<Event> eventData = callEventOwnedByAUser(userEmail);
        return eventMapper.toEventResponseList(eventData);
    }

    // use by above method called
    public List<Event> callEventOwnedByAUser(String userEmail) {
        User user = userRespository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userEmail));

        return attendanceRepository
                .findByUserAndStatus(user, AttendanceStatus.JOINED)
                .stream()
                .map(EventAttendance::getEvent)
                .collect(Collectors.toList());
    }


    // call method bellow  it
    @Override
    public List<EventResponse> filterEvents(String khan, Long categoryId, LocalDate startDate, LocalDate endDate, String title) {
        List<Event> eventsFiltered  = filterEventsData(khan, categoryId, startDate, endDate, title);
        return eventMapper.toEventResponseList(eventsFiltered);
    }

    /**
     * Filter events by various criteria
     */
    public List<Event> filterEventsData(
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

    /**
     * Get all published events (accessible to everyone)
     */
    public List<Event> getAllPublishedEvents() {
        return eventRepository.findByStatus(EventStatus.PUBLISHED);
    }


    @Override
    public List<EventResponse> filterEventsByCategory(Long categoryId, String categoryName) {
        List<Event> filteredEvents = eventRepository.filterEventsByCategory(categoryId, categoryName);
        return eventMapper.toEventResponseList(filteredEvents);
    }
}
