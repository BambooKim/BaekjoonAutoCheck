package com.bamboo.baekjoon.domain.checks.service;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.FailureReason;
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

    @Test
    @DisplayName("Check 수정 - User만 수정")
    public void updateCheck1() {
        // given
        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);

        Users user = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.INACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Checks check = Checks.builder()
                .status(CheckStatus.PENDING)
                .user(user)
                .term(term1)
                .build();
        checkRepository.save(check);

        // when
        CheckRequestDto.Update requestDto1 = CheckRequestDto.Update.builder()
                .termId(term2.getId()).build();
        CheckResponseDto.Detail response1 = checkService.updateCheck(check.getId(), requestDto1);
        System.out.println("response1 = " + response1);

        // then
        assertThat(response1.getId()).isEqualTo(check.getId());
        assertThat(response1.getStatus()).isEqualTo(check.getStatus());
        assertThat(response1.isSuccess()).isEqualTo(check.isSuccess());
        assertThat(response1.getReason()).isEqualTo(check.getReason());
        assertThat(response1.getRunAt()).isEqualTo(check.getRunAt());
        assertThat(response1.getUserInfo().getId()).isEqualTo(check.getUser().getId());
        assertThat(response1.getTermInfo().getId()).isEqualTo(check.getTerm().getId());
    }

    @Test
    @DisplayName("Check 수정 - status, success, reason 수정")
    public void updateCheck2() {
        // given
        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);

        Users user = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.INACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Checks check = Checks.builder()
                .status(CheckStatus.PENDING)
                .user(user)
                .term(term1)
                .build();
        checkRepository.save(check);

        // when
        CheckRequestDto.Update requestDto2 = CheckRequestDto.Update.builder()
                .status(CheckStatus.COMPLETE)
                .success(true)
                .reason(FailureReason.TIER_UNMATCH)
                .build();
        CheckResponseDto.Detail response2 = checkService.updateCheck(check.getId(), requestDto2);
        System.out.println("response2 = " + response2);

        // then
        assertThat(response2.getId()).isEqualTo(check.getId());
        assertThat(response2.getStatus()).isEqualTo(check.getStatus());
        assertThat(response2.isSuccess()).isEqualTo(check.isSuccess());
        assertThat(response2.getReason()).isEqualTo(check.getReason());
        assertThat(response2.getRunAt()).isEqualTo(check.getRunAt());
        assertThat(response2.getUserInfo().getId()).isEqualTo(check.getUser().getId());
        assertThat(response2.getTermInfo().getId()).isEqualTo(check.getTerm().getId());
    }
}