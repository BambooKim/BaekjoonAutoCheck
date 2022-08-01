package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.global.config.security.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDto.Creation> createUser(UserRequestDto.Creation createUserData);
    ResponseEntity<List<UserResponseDto.Status>> updateUserStatus(List<UserRequestDto.Status> list);
    ResponseEntity<List<UserResponseDto.Tier>> updateUserTier(List<Long> userIdList);
    ResponseEntity<?> login(Token.Request request);
}
