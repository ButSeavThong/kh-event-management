package com.thong.event.feature.user;


import com.thong.event.feature.user.dto.CreateUserRequest;
import com.thong.event.feature.user.dto.UpdateProfileRequest;
import com.thong.event.feature.user.dto.UserProfileResponse;

import java.util.List;

public interface UserSerivce {
    void register(CreateUserRequest createUserRequest);
    void deleteUserById(Integer id);
    UserProfileResponse getUserProfile(Integer id);
    UserProfileResponse UpdateProfileById(Integer id, UpdateProfileRequest updateProfileRequest);
    List<UserProfileResponse> getUserProfiles();
}
