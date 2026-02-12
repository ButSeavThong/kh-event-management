package com.thong.event.feature.speaker.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "fullName", "title", "company", "bio", "imageUrl"})
public class SpeakerResponse {

    @NotBlank(message = "Full name is required")
    private Long id;
    private String fullName;
    private String title;
    private String company;
    private String bio;
    private String imageUrl;
}