package com.thong.event.feature.user.dto;

public record UpdateProfileRequest(
        String username,
        String email,
        String profileImage
) {
}
