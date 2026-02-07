package com.thong.event.feature.speaker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpeakerResponse {

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String title;
    private String company;
    private String bio;
    private String imageUrl;
}