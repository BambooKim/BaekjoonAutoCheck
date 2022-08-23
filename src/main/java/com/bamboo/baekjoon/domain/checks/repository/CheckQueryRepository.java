package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.bamboo.baekjoon.domain.checks.QChecks.checks;
import static com.bamboo.baekjoon.domain.term.QTerm.term;
import static com.bamboo.baekjoon.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional
public class CheckQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Checks> searchForRunCheck(RunCheckCondition condition) {
         return queryFactory
                .select(checks)
                .from(checks)
                .join(checks.user, user).fetchJoin()
                .join(checks.term, term).fetchJoin()
                .where(
                        termIdIn(condition.getTermIdList()),
                        userIdIn(condition.getUserIdList()),
                        checkIdIn(condition.getCheckIdList()),
                        checks.status.eq(CheckStatus.PENDING)
                )
                .fetch();
    }

    private BooleanExpression termIdIn(List<Long> termIdList) {
        return termIdList != null ? term.id.in(termIdList) : null;
    }

    private BooleanExpression userIdIn(List<Long> userIdList) {
        return userIdList != null ?
                user.id.in(userIdList).and(term.endAt.before(LocalDateTime.now())) : null;
    }

    private BooleanExpression checkIdIn(List<Long> checkIdList) {
        return checkIdList != null ? checks.id.in(checkIdList) : null;
    }

    @Builder
    @Getter
    public static class RunCheckCondition {

        private List<Long> termIdList;
        private List<Long> userIdList;
        private List<Long> checkIdList;
    }

}
