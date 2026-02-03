package com.thong.event.feature.user.dto;

import java.util.List;

public record UserProfileResponse(
        String username,
        String email,
        Boolean isDeleted,
        String profileImage) {
}
