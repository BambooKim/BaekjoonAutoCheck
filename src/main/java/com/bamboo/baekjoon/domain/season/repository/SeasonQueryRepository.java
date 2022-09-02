package com.bamboo.baekjoon.domain.season.repository;

import com.bamboo.baekjoon.domain.season.Season;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bamboo.baekjoon.domain.rank.QRank.*;
import static com.bamboo.baekjoon.domain.season.QSeason.*;

@Repository
@RequiredArgsConstructor
@Transactional
public class SeasonQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Season> findSeasonByUserId(Long userId) {
        return queryFactory
                .select(season)
                .from(season)
                .join(rank)
                .on(rank.season.eq(season).and(rank.user.id.eq(userId)))
                .fetch();
    }
}
