package com.thong.event.feature.user.dto;


public record UserProfileResponse(
        String username,
        String email,
        Boolean isDeleted,
        String profileImage) {
}
