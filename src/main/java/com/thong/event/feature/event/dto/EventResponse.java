package com.thong.event.feature.event.dto;

import com.thong.event.utils.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record EventResponse(
        Long id,
        String title,
        String description,

        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime,

        String location,
        String khan,

        BigDecimal price,
        Integer capacity,

        EventStatus status,

        List<String> imageUrls,

        Long categoryId,
        String categoryName,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
