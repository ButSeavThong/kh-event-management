package com.thong.event.feature.speaker;
import com.thong.event.domain.Speaker;
import com.thong.event.feature.speaker.dto.SpeakerRequest;
import com.thong.event.feature.speaker.dto.SpeakerResponse;
import com.thong.event.feature.speaker.dto.UpdateSpeakerRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeakerMapper {
    Speaker fromCreateSpeakerRequest(SpeakerRequest speakerRequest);
    SpeakerResponse toSpeakerResponse(Speaker speaker);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSpeakerPartially(UpdateSpeakerRequest updateSpeakerRequest, @MappingTarget Speaker speaker);
    List<SpeakerResponse> toListOfSpeakerResponse(List<Speaker> speakerList);


}
