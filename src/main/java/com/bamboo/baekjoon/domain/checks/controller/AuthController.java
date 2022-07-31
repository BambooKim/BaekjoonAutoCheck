package com.bamboo.baekjoon.domain.checks.controller;

import com.bamboo.baekjoon.domain.user.Role;
import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import com.bamboo.baekjoon.global.config.security.JwtFilter;
import com.bamboo.baekjoon.global.config.security.Token;
import com.bamboo.baekjoon.global.config.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDto.Creation request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 User Id입니다.");

        User user = User.builder()
                .korName(request.getKorName())
                .enterYear(request.getEnterYear())
                .bojId(request.getBojId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.INACTIVE)
                .role(Role.USER)
                .joinedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.Creation.of(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Token.Request request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 사용자입니다.");
        });

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenService.generateToken(user.getId().toString(), user.getRole().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(Token.Response.of(jwt), httpHeaders, HttpStatus.OK);
    }
}
