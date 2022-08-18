package com.bamboo.baekjoon.domain.rank.repository;

import com.bamboo.baekjoon.domain.rank.Rank;
import com.bamboo.baekjoon.domain.season.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Rank> findBySeasonIs(Season season);

    @EntityGraph(attributePaths = {"user"})
    Page<Rank> findPagesBySeasonIs(Season season, Pageable pageable);
}
