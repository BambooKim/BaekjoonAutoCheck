package com.bamboo.baekjoon.domain.user.controller;

import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.service.UserService;
import com.bamboo.baekjoon.global.config.security.Token;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiOperation(value = "회원 가입", notes = "회원 가입을 통해 User를 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "실행 성공"),
            @ApiResponse(responseCode = "400", description = "백준 아이디가 유효하지 않거나 사용중임. 유저 아이디가 이미 사용중임"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto.Creation> createUser(@Valid @RequestBody UserRequestDto.Creation request) {
        UserResponseDto.Creation response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인에 성공하면 JWT를 반환받는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = Token.Response.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "없는 사용자임"),
    })
    public ResponseEntity<?> login(@Valid @RequestBody Token.Request request) {
        return userService.login(request);
    }

    @PutMapping("/admin/user/status")
    @ApiOperation(value = "User 상태 변경", notes = "User의 상태를 변경한다. ACTIVE, INACTIVE")
    public ResponseEntity<List<UserResponseDto.Status>> updateUserStatus(@Valid @RequestBody List<UserRequestDto.Status> list) {
        List<UserResponseDto.Status> response = userService.updateUserStatus(list);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/admin/user/tier")
    @ApiOperation(value = "User Tier 정보 업데이트",
            notes = "userId 리스트에 있는 User들의 solved.ac 티어 정보를 받아와 내부 정보를 업데이트한다.\n" +
                    "Empty List라면 존재하는 전체 User의 티어 정보를 업데이트한다.")
    public ResponseEntity<List<UserResponseDto.Tier>> updateUserTier(@RequestBody List<Long> userIdList) {
        List<UserResponseDto.Tier> response = userService.updateUserTier(userIdList);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
