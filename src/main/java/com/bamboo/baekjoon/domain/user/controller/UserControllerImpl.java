package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.service.UserService;
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

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto.Creation> createUser(@Valid @RequestBody UserRequestDto.Creation createUserData) {
        UserResponseDto.Creation response = userService.createUser(createUserData);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
