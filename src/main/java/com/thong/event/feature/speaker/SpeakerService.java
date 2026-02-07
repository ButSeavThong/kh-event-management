package com.thong.event.feature.speaker;

import com.thong.event.domain.Speaker;
import com.thong.event.feature.speaker.dto.SpeakerRequest;
import com.thong.event.feature.speaker.dto.SpeakerResponse;
import com.thong.event.feature.speaker.dto.UpdateSpeakerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeakerService {
    private final SpeakerMapper speakerMapper;
    private final SpeakerRepository speakerRepository;
    
    public List<SpeakerResponse> getAllSpeakers() {
        List<Speaker> speakers = speakerRepository.findAll();
        return speakerMapper.toListOfSpeakerResponse(speakers);
    }

    @PreAuthorize("permitAll()")
    public SpeakerResponse getSpeakerById(Long id) {
        Speaker speaker =  speakerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Speaker not found with id: " + id));
        return speakerMapper.toSpeakerResponse(speaker);
    }
    
    public List<SpeakerResponse> searchSpeakers(String name) {
        List<Speaker> speakers = speakerRepository.findByFullNameContainingIgnoreCase(name);
        return speakerMapper.toListOfSpeakerResponse(speakers);
    }
    
    @Transactional
    public SpeakerResponse createSpeaker(SpeakerRequest request) {
        Speaker speaker =  speakerMapper.fromCreateSpeakerRequest(request);
        return speakerMapper.toSpeakerResponse(speaker);
    }
    
    @Transactional
    public SpeakerResponse updateSpeaker(Long id, UpdateSpeakerRequest request) {
      Speaker speaker = speakerRepository.findById(id)
              .orElseThrow(()-> new ResponseStatusException(
                      HttpStatus.NOT_FOUND, "Speaker not found with id: " + id
              ));
      speakerMapper.toSpeakerPartially(request,speaker);
      speaker = speakerRepository.save(speaker);
      return  speakerMapper.toSpeakerResponse(speaker);
    }
    
    @Transactional
    public void deleteSpeaker(Long id) {
        Speaker speaker = speakerRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,  "Speaker not found with id: " + id));
        speakerRepository.delete(speaker);
    }
}