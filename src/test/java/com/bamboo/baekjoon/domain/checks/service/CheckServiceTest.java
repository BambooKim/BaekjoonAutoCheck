package com.bamboo.baekjoon.domain.checks.service;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.repository.CheckRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.Users;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
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
class CheckServiceTest {

    @Autowired
    CheckService checkService;

    @Autowired
    CheckRepository checkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("Check 생성")
    public void createCheck() {
        // given
        Users user = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        CheckRequestDto.Create requestDto = CheckRequestDto.Create.builder()
                .userId(user.getId())
                .termId(term.getId())
                .build();

        // when
        CheckResponseDto.Simple responseDto = checkService.createCheck(requestDto);
        System.out.println("responseDto = " + responseDto);

        // then
        assertThat(responseDto.getStatus()).isEqualTo(CheckStatus.PENDING);
        assertThat(responseDto.isSuccess()).isFalse();
        assertThat(responseDto.getUserInfo().getBojId()).isEqualTo("sobu0715");
        assertThat(responseDto.getTermInfo().getId()).isEqualTo(term.getId());
    }

    @Test
    @DisplayName("잘못된 User와 Term으로 Check 생성 시도")
    public void createCheckWrong() {
        // given
        Users user = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        CheckRequestDto.Create requestDto1 = CheckRequestDto.Create.builder()
                .userId(Long.MAX_VALUE)
                .termId(term.getId())
                .build();

        CheckRequestDto.Create requestDto2 = CheckRequestDto.Create.builder()
                .userId(user.getId())
                .termId(Long.MAX_VALUE)
                .build();

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.createCheck(requestDto1));
        assertThrows(ResponseStatusException.class, () -> checkService.createCheck(requestDto2));
    }

    @Test
    @DisplayName("INACTIVE User로 Check 생성 시도")
    public void createCheckWithINACTIVE() {
        // given
        Users user = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.INACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term);

        CheckRequestDto.Create requestDto = CheckRequestDto.Create.builder()
                .userId(user.getId())
                .termId(term.getId())
                .build();

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.createCheck(requestDto));
    }
}