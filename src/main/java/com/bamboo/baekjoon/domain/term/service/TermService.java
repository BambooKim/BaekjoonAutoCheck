package com.bamboo.baekjoon.domain.term.service;

import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermService {
    TermResponseDto createTerm(TermRequestDto requestDto);

    TermResponseDto findTermById(Long id);

    Page<TermResponseDto> getTermAll(Long seasonId, Pageable pageable);

    TermResponseDto updateTerm(Long id, TermRequestDto requestDto);

    String deleteById(Long id);
}
