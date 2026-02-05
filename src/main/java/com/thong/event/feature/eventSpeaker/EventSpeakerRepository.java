package com.thong.event.feature.eventSpeaker;

import com.thong.event.domain.Event;
import com.thong.event.domain.EventSpeaker;
import com.thong.event.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSpeakerRepository extends JpaRepository<EventSpeaker, Long> {
    
    // Find all speakers for an event
    List<EventSpeaker> findByEventOrderByDisplayOrderAsc(Event event);
    
    // Find all events for a speaker
    List<EventSpeaker> findBySpeaker(Speaker speaker);
    
    // Check if a speaker is already added to an event
    Optional<EventSpeaker> findByEventAndSpeaker(Event event, Speaker speaker);
    
    // Count speakers for an event
    long countByEvent(Event event);
    
    // Delete speaker from event
    void deleteByEventAndSpeaker(Event event, Speaker speaker);
    
    // Get speakers by event ID
    @Query("SELECT es FROM EventSpeaker es WHERE es.event.id = :eventId ORDER BY es.displayOrder ASC")
    List<EventSpeaker> findByEventId(Long eventId);

    boolean existsByEventAndSpeaker(Event event, Speaker speaker);
}