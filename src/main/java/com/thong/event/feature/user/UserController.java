package com.thong.event.feature.user;

import com.thong.event.feature.user.dto.UpdateProfileRequest;
import com.thong.event.feature.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserSerivce userService;

    @GetMapping("/profile/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    public UserProfileResponse getProfileById( @PathVariable Integer id) {
        return userService.getUserProfile(id);
    }

    @PatchMapping("update/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateProfileById( @PathVariable Integer id, @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return userService.UpdateProfileById(id, updateProfileRequest);
    }

}
