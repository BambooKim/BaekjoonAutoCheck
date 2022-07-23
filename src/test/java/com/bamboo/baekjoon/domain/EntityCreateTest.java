package com.bamboo.baekjoon.domain;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.FailureReason;
import com.bamboo.baekjoon.domain.rank.Rank;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EntityCreateTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName(value = "User 생성")
    public void createUser() {
        em.clear();

        // given
        Users user = Users.builder()
                .korName("김범구")
                .userTier(13)
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();

        em.persist(user);
        em.flush();
        em.clear();

        // when
        Users findUser = em.createQuery("select u from Users u where u.id = :user_id", Users.class)
                .setParameter("user_id", user.getId())
                        .getResultList().get(0);

        System.out.println("findUser = " + findUser);

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getKorName()).isEqualTo(user.getKorName());
        assertThat(findUser.getUserTier()).isEqualTo(user.getUserTier());
        assertThat(findUser.getEnterYear()).isEqualTo(user.getEnterYear());
        assertThat(findUser.getBojId()).isEqualTo(user.getBojId());
        assertThat(findUser.getStatus()).isEqualTo(user.getStatus());
        assertThat(findUser.getJoinedAt()).isNotNull();
    }

    @Test
    @DisplayName(value = "Term 생성")
    public void createTerm() {
        em.clear();

        // given
        Term term = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 21, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 23, 23, 59, 59))
                .build();

        em.persist(term);
        em.flush();
        em.clear();

        // when
        Term findTerm = em.createQuery("select t from Term t where t.id = :term_id", Term.class)
                .setParameter("term_id", term.getId())
                .getResultList().get(0);

        System.out.println("findTerm = " + findTerm);

        // then
        assertThat(findTerm.getId()).isEqualTo(term.getId());
        assertThat(findTerm.getStartAt()).isEqualTo(term.getStartAt());
        assertThat(findTerm.getEndAt()).isEqualTo(term.getEndAt());
    }

    @Test
    @DisplayName(value = "Rank 생성")
    public void createRank() {
        em.clear();

        // given
        // User 생성
        Users user = Users.builder()
                .korName("김범구")
                .userTier(13)
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        em.persist(user);

        // Rank 생성
        Rank rank = Rank.builder()
                .scoreByLevel(0)
                .scoreByContinuity(0)
                .user(user)
                .build();
        em.persist(rank);

        em.flush();
        em.clear();

        // when
        Rank findRank = em.createQuery("select r from Rank r join fetch r.user where r.id = :rank_id", Rank.class)
                .setParameter("rank_id", rank.getId())
                .getResultList()
                .get(0);

        System.out.println("findRank = " + findRank);

        // then
        assertThat(findRank).usingRecursiveComparison().isEqualTo(rank);
    }

    @Test
    @DisplayName(value = "Checks 생성")
    public void createChecks() {
        em.clear();


        // given
        // User 생성
        Users user = Users.builder()
                .korName("김범구")
                .userTier(13)
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        em.persist(user);

        // Term 생성
        Term term1 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 21, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 23, 23, 59, 59))
                .build();
        Term term2 = Term.builder()
                .startAt(LocalDateTime.of(2022, 7, 24, 0, 0))
                .endAt(LocalDateTime.of(2022, 7, 26, 23, 59, 59))
                .build();
        em.persist(term1);
        em.persist(term2);

        // Checks 생성
        Checks check1 = Checks.builder()
                .status(CheckStatus.PENDING)
                .success(false)
                .runAt(null)
                .user(user)
                .term(term1)
                .build();
        Checks check2 = Checks.builder()
                .status(CheckStatus.COMPLETE)
                .success(false)
                .reason(FailureReason.TIER_UNMATCH)
                .runAt(LocalDateTime.now())
                .user(user)
                .term(term2)
                .build();
        em.persist(check1);
        em.persist(check2);

        // CheckHistory 생성
        CheckHistory chkHistory = CheckHistory.builder()
                .probNo(1024)
                .probTier(13)
                .solvedAt(LocalDateTime.now())
                .user(user)
                .check(check2)
                .build();
        em.persist(chkHistory);

        em.flush();
        em.clear();

        // when
        List<Checks> checks = em.createQuery("select c from Checks c" +
                        " join fetch c.user" +
                        " join fetch c.term", Checks.class)
                .getResultList();

        for (Checks check : checks) {
            System.out.println("check = " + check);
        }

        CheckHistory findChkHistory = em.createQuery("select ch from CheckHistory ch " +
                        " join fetch ch.user where ch.id = :ch_id", CheckHistory.class)
                .setParameter("ch_id", chkHistory.getId())
                .getResultList().get(0);

        System.out.println("findChkHistory = " + findChkHistory);

        // then
        assertThat(checks.get(0)).usingRecursiveComparison().isEqualTo(check1);
        assertThat(checks.get(1)).usingRecursiveComparison().isEqualTo(check2);

        assertThat(findChkHistory).usingRecursiveComparison().isEqualTo(chkHistory);
    }
}