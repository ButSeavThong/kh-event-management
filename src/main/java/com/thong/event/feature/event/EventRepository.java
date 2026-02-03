package com.thong.event.feature.event;

import com.thong.event.domain.Category;
import com.thong.event.domain.Event;
import com.thong.event.utils.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    // Find all events by status
    List<Event> findByStatus(EventStatus status);
    
    // Find events by status and location
    List<Event> findByStatusAndKhan(EventStatus status, String khan);
    
    // Find events by status and category
    List<Event> findByStatusAndCategory(EventStatus status, Category category);
    
    // Find events by status and date range
    List<Event> findByStatusAndStartDateBetween(
        EventStatus status, 
        LocalDate startDate, 
        LocalDate endDate
    );
    
    // Search events by title
    List<Event> findByStatusAndTitleContainingIgnoreCase(
        EventStatus status, 
        String title
    );
}