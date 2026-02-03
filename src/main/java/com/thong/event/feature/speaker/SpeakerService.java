package com.thong.event.feature.speaker;

import com.thong.event.domain.Speaker;
import com.thong.event.exception.ResourceNotFoundException;
import com.thong.event.feature.speaker.dto.SpeakerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeakerService {
    
    private final SpeakerRepository speakerRepository;
    
    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }
    
    public Speaker getSpeakerById(Long id) {
        return speakerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Speaker not found with id: " + id));
    }
    
    public List<Speaker> searchSpeakers(String name) {
        return speakerRepository.findByFullNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public Speaker createSpeaker(SpeakerRequest request) {
        Speaker speaker = Speaker.builder()
            .fullName(request.getFullName())
            .title(request.getTitle())
            .company(request.getCompany())
            .bio(request.getBio())
            .imageUrl(request.getImageUrl())
            .build();
        
        return speakerRepository.save(speaker);
    }
    
    @Transactional
    public Speaker updateSpeaker(Long id, SpeakerRequest request) {
        Speaker speaker = getSpeakerById(id);
        speaker.setFullName(request.getFullName());
        speaker.setTitle(request.getTitle());
        speaker.setCompany(request.getCompany());
        speaker.setBio(request.getBio());
        speaker.setImageUrl(request.getImageUrl());
        return speakerRepository.save(speaker);
    }
    
    @Transactional
    public void deleteSpeaker(Long id) {
        Speaker speaker = getSpeakerById(id);
        speakerRepository.delete(speaker);
    }
}