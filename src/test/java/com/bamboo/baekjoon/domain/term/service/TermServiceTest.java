package com.bamboo.baekjoon.domain.term.service;

import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TermServiceTest {

    @Autowired
    TermService termService;

    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("Term 저장")
    public void createTerm() {
        // given
        TermRequestDto requestDto = TermRequestDto.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();

        // when
        TermResponseDto responseDto = termService.createTerm(requestDto);

        // then
        assertThat(responseDto.getStartAt()).isEqualTo(requestDto.getStartAt());
        assertThat(requestDto.getEndAt()).isEqualTo(requestDto.getEndAt());
    }

    @Test
    @DisplayName("시작과 마감이 바뀐 Term 저장")
    public void createReverseTerm() {
        // given
        TermRequestDto requestDto = TermRequestDto.builder()
                .endAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .startAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            termService.createTerm(requestDto);
        });
    }

    @Test
    @DisplayName("ID로 Term 조회")
    public void findTermById() {
        // given
        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        // when
        TermResponseDto requestDto = termService.findTermById(term.getId());

        // then
        assertThat(requestDto.getId()).isEqualTo(term.getId());
        assertThat(requestDto.getStartAt()).isEqualTo(term.getStartAt());
        assertThat(requestDto.getEndAt()).isEqualTo(term.getEndAt());

    }

    @Test
    @DisplayName("없는 ID로 Term 조회 시도")
    public void findTermByNoId() {
        // given
        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            termService.findTermById(Long.MAX_VALUE);
        });
    }

    @Test
    @DisplayName("Term 수정")
    public void updateTerm() {
        // given
        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        // when
        TermRequestDto requestDto = TermRequestDto.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        TermResponseDto responseDto = termService.updateTerm(term.getId(), requestDto);

        // then
        assertThat(responseDto.getId()).isEqualTo(term.getId());
        assertThat(responseDto.getStartAt()).isEqualTo(requestDto.getStartAt());
        assertThat(responseDto.getEndAt()).isEqualTo(requestDto.getEndAt());
    }

    @Test
    @DisplayName("시작과 마감이 바뀌게 Term 수정 시도")
    public void updateTermReverse() {
        // given
        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        // when
        TermRequestDto requestDto = TermRequestDto.builder()
                .endAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .startAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();

        // then
        assertThrows(ResponseStatusException.class, () -> {
            termService.updateTerm(term.getId(), requestDto);
        });
    }

    @Test
    @DisplayName("없는 ID로 Term 수정 시도")
    public void updateTermByNoId() {
        // given
        TermRequestDto requestDto = TermRequestDto.builder()
                .endAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .startAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            termService.updateTerm(Long.MAX_VALUE, requestDto);
        });
    }

    @Test
    @DisplayName("Term 삭제")
    public void deleteById() {
        // given
        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        // when
        String message = termService.deleteById(term.getId());

        // then
        assertThat(message).isEqualTo("delete success");
        assertThrows(Exception.class, () -> termRepository.findById(term.getId()).get());
    }

    @Test
    @DisplayName("없는 ID로 Term 삭제 시도")
    public void deleteTermByNoId() {
        // given
        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            termService.deleteById(Long.MAX_VALUE);
        });
    }
}