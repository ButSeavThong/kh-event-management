package com.thong.event.feature.user;

import com.thong.event.domain.User;
import com.thong.event.feature.auth.dto.RegisterRequest;
import com.thong.event.feature.user.dto.CreateUserRequest;
import com.thong.event.feature.user.dto.UpdateProfileRequest;
import com.thong.event.feature.user.dto.UserProfileResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromCreateUserRequest(CreateUserRequest createUserRequest);
    User fromRegisterRequest(RegisterRequest registerRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUserPartially(UpdateProfileRequest updateProfileRequest, @MappingTarget User user);
    UserProfileResponse toUserProfileResponse(User user);
    List<UserProfileResponse> toListOfUserProfileResponse(List<User> userList);
}
