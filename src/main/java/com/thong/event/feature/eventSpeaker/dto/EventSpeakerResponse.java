package com.thong.event.feature.eventSpeaker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
public class EventSpeakerResponse {

    private Long speakerId;
    private String fullName;
    private String title;
    private String company;
    private String imageUrl;
    private Integer displayOrder;
    private String role;
    private LocalTime speakingTime;
    private BigDecimal speakerFee;
}
