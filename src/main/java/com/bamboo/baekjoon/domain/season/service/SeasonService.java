package com.bamboo.baekjoon.domain.season.service;

import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;

public interface SeasonService {
    SeasonResponseDto createSeason(SeasonRequestDto requestDto);
}
