package com.thong.event.feature.admin.controller;

import com.thong.event.feature.user.UserSerivce;
import com.thong.event.feature.user.dto.CreateUserRequest;
import com.thong.event.feature.user.dto.UpdateProfileRequest;
import com.thong.event.feature.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor // ready
public class AdminUserController {
    private final UserSerivce userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public List<UserProfileResponse> getUserProfiles() {
        return userService.getUserProfiles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    void createUser( @RequestBody  @Valid    CreateUserRequest createUserRequest) {
        userService.register(createUserRequest);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    UserProfileResponse updateProfile(  @PathVariable Integer id,  @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return userService.UpdateProfileById(id, updateProfileRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserProfileResponse getUserProfile(  @PathVariable Integer id) {
        return userService.getUserProfile(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
    }
}
