package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.global.config.security.Token;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserResponseDto.Creation createUser(UserRequestDto.Creation createUserData);

    List<UserResponseDto.Tier> updateUserTier(List<Long> userIdList);

    List<UserResponseDto.Status> updateUserStatus(List<UserRequestDto.Status> list);

    ResponseEntity<?> login(Token.Request request);
}
