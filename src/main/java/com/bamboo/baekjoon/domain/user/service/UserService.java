package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto.Creation createUser(UserRequestDto.Creation createUserData);

    List<UserResponseDto.Tier> updateUserTier(List<Long> userIdList);
}
