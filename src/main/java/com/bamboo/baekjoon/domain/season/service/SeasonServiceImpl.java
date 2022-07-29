package com.bamboo.baekjoon.domain.season.service;

import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;

    @Override
    public SeasonResponseDto createSeason(SeasonRequestDto requestDto) {
        Season season = Season.builder()
                .name(requestDto.getName())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .build();

        seasonRepository.save(season);

        return SeasonResponseDto.of(season);
    }
}
