package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto.Creation createUser(UserRequestDto.Creation createUserData);
}
