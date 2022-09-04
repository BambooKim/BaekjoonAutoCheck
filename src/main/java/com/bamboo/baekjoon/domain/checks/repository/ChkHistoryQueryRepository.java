package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;

import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.dto.QCheckResponseDto_History;
import com.bamboo.baekjoon.domain.term.Term;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

    public Page<CheckResponseDto.History> searchHistoryByUserAndCheck(List<Long> checkId, Pageable pageable) {
        List<CheckResponseDto.History> content = queryFactory
                .select(new QCheckResponseDto_History(
                        checkHistory.id,
                        checkHistory.probNo,
                        checkHistory.probTier,
                        checkHistory.solvedAt
                ))
                .from(checkHistory)
                .where(checkHistory.check.id.in(checkId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(checkHistory.count())
                .from(checkHistory)
                .where(checkHistory.check.id.in(checkId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

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
