package com.thong.event.feature.speaker.dto;
import lombok.Data;

@Data
public class UpdateSpeakerRequest {
    private String fullName;
    private String title;
    private String company;
    private String bio;
    private String imageUrl;
}
