package com.bamboo.baekjoon.domain.term.service;

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

@Service
@Transactional
@RequiredArgsConstructor
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;

    @Override
    public TermResponseDto createTerm(TermRequestDto requestDto) {
        Term term = Term.builder().startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt()).build();

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
    public Page<TermResponseDto> getTermAll(Pageable pageable) {
        return termRepository.findAll(pageable).map(m -> TermResponseDto.builder()
                .id(m.getId())
                .startAt(m.getStartAt())
                .endAt(m.getEndAt()).build());
    }

    @Override
    public TermResponseDto updateTerm(Long id, TermRequestDto requestDto) {
        Term findTerm = termRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Term이 존재하지 않습니다.");
        });

        validateDateTime(requestDto);

        findTerm.changeTerm(requestDto);

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
