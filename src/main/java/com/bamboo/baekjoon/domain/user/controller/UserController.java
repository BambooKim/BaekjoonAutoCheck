package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDto.Creation> createUser(UserRequestDto.Creation createUserData);
    ResponseEntity<List<UserResponseDto.Tier>> updateUserTier(List<Long> userIdList);
}
