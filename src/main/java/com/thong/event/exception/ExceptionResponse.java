package com.thong.event.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse<T>(
        String message,
        Integer status,
        LocalDateTime timestamp,
        T detail
) {
}
