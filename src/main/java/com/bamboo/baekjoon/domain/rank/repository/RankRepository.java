package com.bamboo.baekjoon.domain.rank.repository;

import com.bamboo.baekjoon.domain.rank.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
