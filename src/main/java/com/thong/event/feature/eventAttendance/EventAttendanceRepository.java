package com.thong.event.feature.eventAttendance;

import com.thong.event.domain.Event;
import com.thong.event.domain.EventAttendance;
import com.thong.event.domain.User;
import com.thong.event.utils.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
    
    // Find attendance record for a user and event
    Optional<EventAttendance> findByUserAndEvent(User user, Event event);
    
    // Get all events joined by a user
    List<EventAttendance> findByUserAndStatus(User user, AttendanceStatus status);
    
    // Count attendees for an event
    long countByEventAndStatus(Event event, AttendanceStatus status);
    
    // Get most popular events (for dashboard)
    @Query("SELECT e.event, COUNT(e) as cnt FROM EventAttendance e " +
           "WHERE e.status = :status " +
           "GROUP BY e.event ORDER BY cnt DESC")
    List<Object[]> findMostPopularEvents(@Param("status") AttendanceStatus status);
}