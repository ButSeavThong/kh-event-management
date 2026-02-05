package com.thong.event.feature.eventSpeaker.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReorderSpeakersRequest {
    @NotEmpty(message = "Speaker IDs list cannot be empty")
    private List<Long> speakerIds;  // IDs in desired order: [3, 1, 2]
}