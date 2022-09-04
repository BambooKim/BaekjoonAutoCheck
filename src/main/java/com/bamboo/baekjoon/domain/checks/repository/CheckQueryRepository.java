package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.dto.QCheckResponseDto_UserSeason;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

    public Page<CheckResponseDto.UserSeason> findCheckUserSeason(Long userId, Long seasonId, Pageable pageable) {
        List<CheckResponseDto.UserSeason> content = queryFactory
                .select(new QCheckResponseDto_UserSeason(
                        term.id.as("termId"),
                        checks.id.as("checkId"),
                        checks.status,
                        checks.success,
                        checks.reason,
                        checks.runAt,
                        term.startAt,
                        term.endAt
                ))
                .from(checks)
                .join(checks.term, term)
                .on(checks.user.id.eq(userId).and(term.season.id.eq(seasonId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(checks.count())
                .from(checks)
                .join(checks.term, term)
                .on(checks.user.id.eq(userId).and(term.season.id.eq(seasonId)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Long countFailureCheck(Long userId, Long seasonId) {
        return queryFactory
                .select(checks.count())
                .from(checks)
                .where(checks.user.id.eq(userId)
                        .and(checks.term.season.id.eq(seasonId))
                        .and(checks.success.isFalse()))
                .fetchOne();
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
