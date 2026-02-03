package com.thong.event.feature.admin.dto;

import com.thong.event.domain.Event;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPopularityDTO {
    private Event event;
    private long attendeeCount;
}