package com.thong.event.feature.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private long totalUsers;
    private long totalEvents;
    private List<EventPopularityDTO> mostPopularEvents;
}