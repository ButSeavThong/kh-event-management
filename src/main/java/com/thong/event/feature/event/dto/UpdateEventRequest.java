package com.thong.event.feature.event.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UpdateEventRequest(

        @Size(max = 150, message = "Title must not exceed 150 characters")
        String title,

        String description,

        @Future(message = "Start date must be in the future")
        LocalDate startDate,

        LocalDate endDate,

        LocalTime startTime,
        LocalTime endTime,

        String location,
        String khan,

        @DecimalMin(value = "0.0", message = "Price must be at least 0")
        BigDecimal price,

        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        Long categoryId,

        List<String> imageUrls
) {
}
