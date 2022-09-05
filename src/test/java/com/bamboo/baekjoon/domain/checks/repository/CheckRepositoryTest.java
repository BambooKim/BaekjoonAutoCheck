package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.FailureReason;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.user.Role;
import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CheckRepositoryTest {

    @Autowired
    CheckRepository checkRepository;

    @PersistenceContext
    EntityManager em;

    private User user;
    private List<Season> seasons = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();
    private List<Checks> checks = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user = User.builder()
                .korName("user1")
                .bojId("rlaqjarn1008")
                .enterYear(2018)
                .userTier(10)
                .username("rlaqjarn1008")
                .password("passwordStr")
                .role(Role.ADMIN)
                .joinedAt(LocalDateTime.now()).build();
        user.changeStatus(UserStatus.ACTIVE);
        em.persist(user);

        Season season1 = Season.builder()
                .startAt(LocalDateTime.of(2022, 7, 1, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 8, 31, 23, 59, 59))
                .name("season1")
                .build();
        em.persist(season1);
        seasons.add(season1);

        Season season2 = Season.builder()
                .startAt(LocalDateTime.of(2022, 9, 1, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 12, 31, 23, 59, 59))
                .name("season2")
                .build();
        em.persist(season2);
        seasons.add(season2);

        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 8, 3, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 8, 5, 23, 59, 59))
                .season(season1)
                .build();
        em.persist(term1);
        terms.add(term1);

        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 8, 6, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 8, 8, 23, 59, 59))
                .season(season1)
                .build();
        em.persist(term2);
        terms.add(term2);

        Term term3 = Term.builder()
                .startAt(LocalDateTime.of(2022, 9, 5, 0, 0, 0))
                .endAt(LocalDateTime.of(2022, 9, 7, 23, 59, 59))
                .season(season2)
                .build();
        em.persist(term3);
        terms.add(term3);

        Checks check1 = Checks.builder()
                .user(user)
                .status(CheckStatus.COMPLETE)
                .success(true)
                .reason(null)
                .build();
        check1.setTerm(term1);
        em.persist(check1);
        checks.add(check1);

        Checks check2 = Checks.builder()
                .user(user)
                .status(CheckStatus.COMPLETE)
                .success(false)
                .reason(FailureReason.NO_SUCCESS)
                .build();
        check2.setTerm(term2);
        em.persist(check2);
        checks.add(check2);

        Checks check3 = Checks.builder()
                .user(user)
                .status(CheckStatus.PENDING)
                .success(false)
                .reason(null)
                .build();
        check3.setTerm(term3);
        em.persist(check3);
        checks.add(check3);

        CheckHistory checkHistory = CheckHistory.builder()
                .probNo(10).probNo(10).solvedAt(LocalDateTime.now()).build();
        checkHistory.setCheck(check1);
        em.persist(checkHistory);

        em.clear();
    }

    @Test
    @DisplayName("Check_Status와 Term으로 Check 찾기")
    void findByStatusIsAndTermIn() {
        // given

        // when
        List<Checks> findChecks = checkRepository.findByStatusIsAndTermIn(CheckStatus.COMPLETE, terms);

        // then
        assertThat(findChecks).extracting(Checks::getId).containsExactly(checks.get(0).getId(), checks.get(1).getId());

        for (Checks findCheck : findChecks) {
            boolean isUserLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(findCheck.getUser());
            boolean isHistoryLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(findCheck.getCheckHistoryList());
            assertThat(isUserLoaded).isTrue();
            assertThat(isHistoryLoaded).isTrue();
        }
    }

    @Test
    @DisplayName("User와 Term을 이용해 페이징된 Check 조회")
    void findByUserInAndTermIn() {
        // given
        Pageable pageable = PageRequest.of(0, 20);

        // when
        Page<Checks> findChecksPaged = checkRepository.findByUserInAndTermIn(List.of(user), terms, pageable);

        // then
        boolean isSeasonLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findChecksPaged.getContent().get(0).getTerm().getSeason());
        assertThat(isSeasonLoaded).isTrue();

        assertThat(findChecksPaged.getContent())
                .usingRecursiveComparison()
                .isEqualTo(checks);
        assertThat(findChecksPaged.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("User를 이용해 페이징된 Check 조회")
    void findByUserIn() {
        // given
        Pageable pageable = PageRequest.of(0, 20);

        // when
        Page<Checks> findChecksPaged = checkRepository.findByUserIn(List.of(user), pageable);

        // then
        boolean isTermLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findChecksPaged.getContent().get(0).getTerm());
        assertThat(isTermLoaded).isTrue();

        boolean isUserLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findChecksPaged.getContent().get(0).getUser());
        assertThat(isUserLoaded).isTrue();

        assertThat(findChecksPaged.getContent())
                .extracting(Checks::getId)
                .containsAll(checks.stream().map(Checks::getId).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Term을 이용해 페이징된 Check 조회")
    void findByTermIn() {
        // given
        Pageable pageable = PageRequest.of(0, 20);

        // when
        Page<Checks> findChecksPaged = checkRepository.findByTermIn(List.of(terms.get(0)), pageable);

        // then
        boolean isTermLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findChecksPaged.getContent().get(0).getTerm());
        assertThat(isTermLoaded).isTrue();

        boolean isUserLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findChecksPaged.getContent().get(0).getUser());
        assertThat(isUserLoaded).isTrue();

        assertThat(findChecksPaged.getTotalElements()).isEqualTo(1);
        assertThat(findChecksPaged.getContent())
                .extracting(Checks::getId)
                .containsExactly(checks.get(0).getId());
    }

    @Test
    @DisplayName("findById - User와 Term 로드되었는지 확인")
    void findById() {
        // given

        // when
        Checks findCheck = checkRepository.findById(this.checks.get(1).getId()).orElseThrow(() -> {
            fail("Check 없음");
            return new RuntimeException();
        });

        // then
        boolean isTermLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findCheck.getTerm());
        assertThat(isTermLoaded).isTrue();

        boolean isUserLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil()
                .isLoaded(findCheck.getUser());
        assertThat(isUserLoaded).isTrue();
    }

    @Test
    @DisplayName("id가 리스트 안에 있는 Check 개수 세기")
    void countByIdIn() {
        // given
        List<Long> idList = checks.stream().map(Checks::getId).collect(Collectors.toList());

        // when
        long count = checkRepository.countByIdIn(idList);

        // then
        assertThat(count).isEqualTo(idList.size());
    }

    @Test
    @DisplayName("리스트로 전달한 Check들을 모두 벌크로 삭제")
    void deleteChecksByIdIn() {
        // given
        em.clear();
        List<Long> checkIdList = checks.stream().map(Checks::getId).collect(Collectors.toList());

        // when
        checkRepository.deleteChecksByIdIn(checkIdList);

        // then
        assertThat(checkRepository.count()).isEqualTo(0L);
    }
}