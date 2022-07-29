package com.bamboo.baekjoon.domain.term.controller;

import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TermController {

    ResponseEntity<TermResponseDto> createTerm(TermRequestDto requestDto);
    ResponseEntity<TermResponseDto> searchTermById(Long id);
    ResponseEntity<Page<TermResponseDto>> getTermAll(Long seasonId, Pageable pageable);
    ResponseEntity<TermResponseDto> updateTerm(Long id, TermRequestDto requestDto);
    ResponseEntity<String> deleteTerm(Long id);

}
