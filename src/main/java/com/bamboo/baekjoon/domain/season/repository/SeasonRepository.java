package com.bamboo.baekjoon.domain.season.repository;

import com.bamboo.baekjoon.domain.season.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
