package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.term.Term;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bamboo.baekjoon.domain.checks.QCheckHistory.checkHistory;
import static com.bamboo.baekjoon.domain.checks.QChecks.checks;
import static com.bamboo.baekjoon.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional
public class ChkHistoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CheckHistory> searchHistory(List<Term> terms) {
        return queryFactory
                .select(checkHistory)
                .from(checkHistory)
                .join(checkHistory.check, checks).fetchJoin()
                .join(checks.user, user).fetchJoin()
                .where(checkHistory.check.term.in(terms))
                .fetch();
    }
}
