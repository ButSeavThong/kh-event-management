package com.thong.event.feature.event;

import com.thong.event.domain.Event;
import com.thong.event.feature.event.dto.EventRequest;
import com.thong.event.feature.event.dto.EventResponse;
import com.thong.event.feature.event.dto.UpdateEventRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEvent(EventRequest eventRequest);
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    EventResponse toEventResponse(Event event);
    List<EventResponse> toEventResponseList(List<Event> events);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEventPartially(UpdateEventRequest updateEventRequest, @MappingTarget Event event);

}
