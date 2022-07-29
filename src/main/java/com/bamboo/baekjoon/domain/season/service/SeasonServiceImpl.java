package com.bamboo.baekjoon.domain.season.service;

import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;

    @Override
    public SeasonResponseDto createSeason(SeasonRequestDto requestDto) {
        validateDateTime(requestDto);

        Season season = Season.builder()
                .name(requestDto.getName())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .build();
        seasonRepository.save(season);

        return SeasonResponseDto.of(season);
    }

    private void validateDateTime(SeasonRequestDto requestDto) {
        if (requestDto.getStartAt().isAfter(requestDto.getEndAt()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜와 종료 날짜가 유효하지 않습니다.");
    }
}
