package com.thong.event.feature.admin;
import com.thong.event.feature.admin.dto.DashboardResponse;
import com.thong.event.feature.admin.dto.EventPopularityDTO;
import com.thong.event.feature.event.EventRepository;
import com.thong.event.feature.eventAttendance.EventAttendanceRepository;
import com.thong.event.feature.user.UserRespository;
import com.thong.event.utils.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final UserRespository userRespository;
    private final EventRepository eventRepository;
    private final EventAttendanceRepository attendanceRepository;
    
    /**
     * Get dashboard statistics for admin
     */
    public DashboardResponse getDashboardStats() {
        long totalUsers = userRespository.count();
        long totalEvents = eventRepository.count();
        
        // Get most popular events
        List<Object[]> popularEventsData = 
            attendanceRepository.findMostPopularEvents(AttendanceStatus.JOINED);
        
        List<EventPopularityDTO> popularEvents = popularEventsData.stream()
            .limit(10)  // Top 10 events
            .map(data -> EventPopularityDTO.builder()
                .event((com.thong.event.domain.Event) data[0])
                .attendeeCount(((Number) data[1]).longValue())
                .build())
            .collect(Collectors.toList());
        
        return DashboardResponse.builder()
            .totalUsers(totalUsers)
            .totalEvents(totalEvents)
            .mostPopularEvents(popularEvents)
            .build();
    }
}