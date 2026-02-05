package com.thong.event.feature.eventSpeaker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class UpdateSpeakerInEventRequest {
    private Integer displayOrder;       // Order to display (1, 2, 3...)
    private String role;                // "Keynote", "Panelist", "Workshop Leader"
    private LocalTime speakingTime;     // e.g., "14:30" for 2:30 PM
    private BigDecimal speakerFee;      // How much they're paid
}
