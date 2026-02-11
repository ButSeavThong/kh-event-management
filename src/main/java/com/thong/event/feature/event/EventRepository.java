package com.thong.event.feature.event;

import com.thong.event.domain.Category;
import com.thong.event.domain.Event;
import com.thong.event.utils.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find all events by status
    Page<Event> getEventByStatus(EventStatus status, Pageable pageable);

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

    @Query("""
    SELECT e FROM Event e
    JOIN e.category c
    WHERE 
        (:categoryId IS NULL OR c.id = :categoryId)
    AND
        (:categoryName IS NULL OR c.name ILIKE :categoryName)
""")
    List<Event> filterEventsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("categoryName") String categoryName
    );}