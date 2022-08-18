package com.bamboo.baekjoon.domain.season.service;

import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;

import java.util.List;

public interface SeasonService {
    SeasonResponseDto createSeason(SeasonRequestDto requestDto);

    Long addSeasonUser(Long seasonId, List<Long> userIdList);
}
