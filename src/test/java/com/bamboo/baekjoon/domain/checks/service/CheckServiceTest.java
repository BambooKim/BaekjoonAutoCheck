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
import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("Check 생성 - Multiple")
    public void createMultipleChecks() {
        // given
        Users user1 = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user2 = Users.builder()
                .korName("최성원")
                .userTier(11)
                .enterYear(2018)
                .bojId("choi5798")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user3 = Users.builder()
                .korName("장재훈")
                .userTier(11)
                .enterYear(2019)
                .bojId("jae_hooni")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 6, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 6, 25, 23, 59, 59))
                .build();
        Term term3 = Term.builder()
                .startAt(LocalDateTime.of(2022, 5, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 5, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);
        termRepository.save(term3);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        List<Term> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);
        terms.add(term3);

        CheckRequestDto.CreateList createList = new CheckRequestDto.CreateList(new ArrayList<>());
        for (Users user : users) {
            for (Term term : terms) {
                CheckRequestDto.Create item = CheckRequestDto.Create.builder().userId(user.getId()).termId(term.getId()).build();
                createList.getItems().add(item);
            }
        }

        // when
        List<CheckResponseDto.Simple> responseList = checkService.createChecks(createList);
        for (CheckResponseDto.Simple simple : responseList) {
            System.out.println("simple = " + simple);
        }

        // then
        assertThat(responseList.get(0).getUserInfo().getId()).isEqualTo(user1.getId());
        assertThat(responseList.get(0).getTermInfo().getId()).isEqualTo(term1.getId());
    }

    @Test
    @DisplayName("Check 생성 - Multiple: INACTIVE 한명 존재할때")
    public void createMultipleChecksWithINACTIVE() {
        // given
        Users user1 = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user2 = Users.builder()
                .korName("최성원")
                .userTier(11)
                .enterYear(2018)
                .bojId("choi5798")
                .status(UserStatus.INACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user3 = Users.builder()
                .korName("장재훈")
                .userTier(11)
                .enterYear(2019)
                .bojId("jae_hooni")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 6, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 6, 25, 23, 59, 59))
                .build();
        Term term3 = Term.builder()
                .startAt(LocalDateTime.of(2022, 5, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 5, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);
        termRepository.save(term3);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        List<Term> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);
        terms.add(term3);

        CheckRequestDto.CreateList createList = new CheckRequestDto.CreateList(new ArrayList<>());
        for (Users user : users) {
            for (Term term : terms) {
                CheckRequestDto.Create item = CheckRequestDto.Create.builder().userId(user.getId()).termId(term.getId()).build();
                createList.getItems().add(item);
            }
        }

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.createChecks(createList));
    }

    @Test
    @DisplayName("Check 생성 - Multiple: 유효하지 않은 userId값")
    public void createMultipleChecksWithInvalidId() {
        // given
        Users user1 = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user2 = Users.builder()
                .korName("최성원")
                .userTier(11)
                .enterYear(2018)
                .bojId("choi5798")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 6, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 6, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(Users.builder().id(Long.MAX_VALUE).build());

        List<Term> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);

        CheckRequestDto.CreateList createList = new CheckRequestDto.CreateList(new ArrayList<>());
        for (Users user : users) {
            for (Term term : terms) {
                CheckRequestDto.Create item = CheckRequestDto.Create.builder().userId(user.getId()).termId(term.getId()).build();
                createList.getItems().add(item);
            }
        }

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.createChecks(createList));
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

    @Test
    @DisplayName("Check 삭제")
    public void deleteCheck() {
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

        Checks check = Checks.builder().status(CheckStatus.PENDING).success(false).user(user).term(term).build();
        checkRepository.save(check);

        // when
        String message = checkService.deleteById(check.getId());

        // then
        assertThat(message).isEqualTo("delete success");
        assertThat(checkRepository.findById(check.getId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("없는 Id로 Check 삭제 시도")
    public void deleteCheckByNoId() {
        assertThrows(ResponseStatusException.class, () -> checkService.deleteById(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("Check 삭제 - List로")
    public void deleteChecksByList() {
        // given
        Users user1 = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user2 = Users.builder()
                .korName("최성원")
                .userTier(11)
                .enterYear(2018)
                .bojId("choi5798")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 6, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 6, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<Term> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);

        List<Long> checkIdList = new ArrayList<>();
        for (Users user : users) {
            for (Term term : terms) {
                Checks check = Checks.builder().status(CheckStatus.PENDING).success(false).user(user).term(term).build();
                checkRepository.save(check);
                checkIdList.add(check.getId());
            }
        }

        // when
        String message = checkService.deleteByParams(checkIdList);

        // then
        assertThat(message).isEqualTo("delete success");
        assertThat(checkRepository.countByList(checkIdList)).isEqualTo(0);
    }

    @Test
    @DisplayName("Check 삭제 - List에 없는 Id 존재")
    public void deleteChecksByListWithNoId() {
        // given
        Users user1 = Users.builder()
                .korName("박민재")
                .userTier(11)
                .enterYear(2017)
                .bojId("sobu0715")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        Users user2 = Users.builder()
                .korName("최성원")
                .userTier(11)
                .enterYear(2018)
                .bojId("choi5798")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 25, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 6, 23, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 6, 25, 23, 59, 59))
                .build();
        termRepository.save(term1);
        termRepository.save(term2);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<Term> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);

        List<Long> checkIdList = new ArrayList<>();
        checkIdList.add(Long.MAX_VALUE);
        for (Users user : users) {
            for (Term term : terms) {
                Checks check = Checks.builder().status(CheckStatus.PENDING).success(false).user(user).term(term).build();
                checkRepository.save(check);
                checkIdList.add(check.getId());
            }
        }

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.deleteByParams(checkIdList));
    }

    @Test
    @DisplayName("Check 삭제 - 빈 List")
    public void deleteChecksByEmptyList() {
        // given
        List<Long> checkIdList = new ArrayList<>();

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> checkService.deleteByParams(checkIdList));
    }
}