package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<UserResponseDto.Creation> createUser(UserRequestDto.Creation createUserData);
}
