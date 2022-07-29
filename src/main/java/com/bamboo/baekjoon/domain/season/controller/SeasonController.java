package com.bamboo.baekjoon.domain.season.controller;

import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import org.springframework.http.ResponseEntity;

public interface SeasonController {

    ResponseEntity<SeasonResponseDto> createSeason(SeasonRequestDto requestDto);
}
