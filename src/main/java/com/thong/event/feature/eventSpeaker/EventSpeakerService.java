package com.thong.event.feature.eventSpeaker;

import com.thong.event.domain.Event;
import com.thong.event.domain.EventSpeaker;
import com.thong.event.domain.Speaker;

import com.thong.event.feature.event.EventRepository;
import com.thong.event.feature.eventSpeaker.dto.AddSpeakerRequest;
import com.thong.event.feature.eventSpeaker.dto.EventSpeakerResponse;
import com.thong.event.feature.eventSpeaker.dto.UpdateSpeakerInEventRequest;
import com.thong.event.feature.speaker.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventSpeakerService {
    
    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final EventSpeakerRepository eventSpeakerRepository;
    
    /**
     * Add a speaker to an event
     * ADMIN only operation
     */
    @Transactional
    public void addSpeakerToEvent(Long eventId, AddSpeakerRequest request) {

        // 1. Validate event exists
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event not found with id: " + eventId
                ));

        // 2. Validate speaker exists
        Speaker speaker = speakerRepository.findById(request.getSpeakerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Speaker not found with id: " + request.getSpeakerId()
                ));

        // 3. Check if speaker already added
        boolean exists = eventSpeakerRepository.existsByEventAndSpeaker(event, speaker);
        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Speaker already exists in this event"
            );
        }

        // 4. Auto display order
        long currentCount = eventSpeakerRepository.countByEvent(event);

        // 5. Save relation
        EventSpeaker eventSpeaker = EventSpeaker.builder()
                .event(event)
                .speaker(speaker)
                .displayOrder(
                        request.getDisplayOrder() != null
                                ? request.getDisplayOrder()
                                : (int) currentCount + 1
                )
                .role(request.getRole())
                .speakingTime(request.getSpeakingTime())
                .speakerFee(request.getSpeakerFee())
                .build();

        eventSpeakerRepository.save(eventSpeaker);
    }


    /**
     * Remove a speaker from an event
     * ADMIN only operation
     */
    @Transactional
    public void removeSpeakerFromEvent(Long eventId, Long speakerId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + eventId));

        Speaker speaker = speakerRepository.findById(speakerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Speaker not found with id: " + speakerId));

        EventSpeaker eventSpeaker = eventSpeakerRepository
            .findByEventAndSpeaker(event, speaker)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Speaker is not associated with this event"));
        
        eventSpeakerRepository.delete(eventSpeaker);
    }
    
    /**
     * Get all speakers for an event (PUBLIC access)
     * Returns speakers ordered by displayOrder
     */
    public List<EventSpeakerResponse> getEventSpeakers(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(
             HttpStatus.NOT_FOUND,   "Event not found with id: " + eventId));
        
        List<EventSpeaker> eventSpeakers = 
            eventSpeakerRepository.findByEventOrderByDisplayOrderAsc(event);
        
        return eventSpeakers.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all events for a speaker
     */
    public List<Event> getSpeakerEvents(Long speakerId) {
        Speaker speaker = speakerRepository.findById(speakerId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Speaker not found with id: " + speakerId));
        
        return eventSpeakerRepository.findBySpeaker(speaker)
            .stream()
            .map(EventSpeaker::getEvent)
            .collect(Collectors.toList());
    }
    
    /**
     * Update speaker metadata for an event
     * (display order, role, speaking time, etc.)
     */
    @Transactional
    public EventSpeaker updateEventSpeaker(
            Long eventId, 
            Long speakerId, 
            UpdateSpeakerInEventRequest request) {
        
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Event not found with id: " + eventId));
        
        Speaker speaker = speakerRepository.findById(speakerId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                "Speaker not found with id: " + speakerId));
        
        EventSpeaker eventSpeaker = eventSpeakerRepository
            .findByEventAndSpeaker(event, speaker)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                "Speaker is not associated with this event"));
        
        // Update metadata
        if (request.getDisplayOrder() != null) {
            eventSpeaker.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getRole() != null) {
            eventSpeaker.setRole(request.getRole());
        }
        if (request.getSpeakingTime() != null) {
            eventSpeaker.setSpeakingTime(request.getSpeakingTime());
        }
        if (request.getSpeakerFee() != null) {
            eventSpeaker.setSpeakerFee(request.getSpeakerFee());
        }
        
        return eventSpeakerRepository.save(eventSpeaker);
    }
    
    /**
     * Reorder speakers for an event
     */
    @Transactional
    public void reorderSpeakers(Long eventId, List<Long> speakerIdsInOrder) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                "Event not found with id: " + eventId));
        
        for (int i = 0; i < speakerIdsInOrder.size(); i++) {
            Long speakerId = speakerIdsInOrder.get(i);
            Speaker speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                    "Speaker not found with id: " + speakerId));
            
            EventSpeaker eventSpeaker = eventSpeakerRepository
                .findByEventAndSpeaker(event, speaker)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                    "Speaker is not associated with this event"));
            
            eventSpeaker.setDisplayOrder(i + 1);
            eventSpeakerRepository.save(eventSpeaker);
        }
    }
    
    // Helper method to convert to DTO
    private EventSpeakerResponse toResponse(EventSpeaker eventSpeaker) {
        return EventSpeakerResponse.builder()
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