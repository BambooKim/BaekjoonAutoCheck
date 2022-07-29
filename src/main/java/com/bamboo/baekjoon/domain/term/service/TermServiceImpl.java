package com.bamboo.baekjoon.domain.term.service;

import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;

import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;
    private final SeasonRepository seasonRepository;

    @Override
    public TermResponseDto createTerm(TermRequestDto requestDto) {
        Season season = seasonRepository.findById(requestDto.getSeasonId()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Season이 존재하지 않습니다.");
        });
        Term term = Term.builder().startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt()).season(season).build();

        // startAt, endAt 비교
        validateDateTime(requestDto);

        termRepository.save(term);

        return TermResponseDto.of(term);
    }

    @Override
    public TermResponseDto findTermById(Long id) {
        Term findTerm = termRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Term이 존재하지 않습니다.");
        });

        return TermResponseDto.of(findTerm);
    }

    @Override
    public Page<TermResponseDto> getTermAll(Long seasonId, Pageable pageable) {
        Season season = seasonRepository.findById(seasonId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Season이 존재하지 않습니다.");
        });
        return termRepository.findAllBySeasonIs(season, pageable).map(m -> TermResponseDto.builder()
                .id(m.getId())
                .startAt(m.getStartAt())
                .endAt(m.getEndAt()).build());
    }

    @Override
    public TermResponseDto updateTerm(Long id, TermRequestDto requestDto) {
        Term findTerm = termRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Term이 존재하지 않습니다.");
        });

        if (!Objects.equals(findTerm.getSeason().getId(), requestDto.getSeasonId())) {
            Season findSeason = seasonRepository.findById(requestDto.getSeasonId()).orElseThrow(() -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Season이 존재하지 않습니다.");
            });
            findTerm.changeSeason(findSeason);
        }

        validateDateTime(requestDto);

        findTerm.changePeriod(requestDto);

        return TermResponseDto.of(findTerm);
    }

    @Override
    public String deleteById(Long id) {
        termRepository.findById(id).ifPresentOrElse(termRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Term이 존재하지 않습니다.");
        });

        return "delete success";
    }

    private void validateDateTime(TermRequestDto requestDto) {
        if (requestDto.getStartAt().isAfter(requestDto.getEndAt()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜와 마감 날짜가 유효하지 않습니다.");
    }
}
