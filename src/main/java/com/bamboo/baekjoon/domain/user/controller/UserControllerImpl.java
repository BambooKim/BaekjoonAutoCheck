package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.service.UserService;
import com.bamboo.baekjoon.global.config.security.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto.Creation> createUser(@Valid @RequestBody UserRequestDto.Creation request) {
        UserResponseDto.Creation response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Token.Request request) {
        return userService.login(request);
    }

    @PutMapping("/user/status")
    public ResponseEntity<List<UserResponseDto.Status>> updateUserStatus(@Valid @RequestBody List<UserRequestDto.Status> list) {
        List<UserResponseDto.Status> response = userService.updateUserStatus(list);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/user/tier")
    public ResponseEntity<List<UserResponseDto.Tier>> updateUserTier(@RequestBody List<Long> userIdList) {
        List<UserResponseDto.Tier> response = userService.updateUserTier(userIdList);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
