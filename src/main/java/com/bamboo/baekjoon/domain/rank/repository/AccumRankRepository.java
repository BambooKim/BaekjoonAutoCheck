package com.bamboo.baekjoon.domain.rank.repository;

import com.bamboo.baekjoon.domain.rank.AccumRank;
import com.bamboo.baekjoon.domain.season.Season;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccumRankRepository extends JpaRepository<AccumRank, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<AccumRank> findBySeasonIs(Season season);
}
