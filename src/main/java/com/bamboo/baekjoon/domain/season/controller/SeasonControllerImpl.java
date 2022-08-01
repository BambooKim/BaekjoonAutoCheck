package com.bamboo.baekjoon.domain.season.controller;

import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import com.bamboo.baekjoon.domain.season.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SeasonControllerImpl implements SeasonController {

    private final SeasonService seasonService;

    @PostMapping("/admin/season")
    public ResponseEntity<SeasonResponseDto> createSeason(@RequestBody SeasonRequestDto requestDto) {
        SeasonResponseDto response = seasonService.createSeason(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
