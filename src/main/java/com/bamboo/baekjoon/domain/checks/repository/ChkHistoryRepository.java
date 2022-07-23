package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChkHistoryRepository extends JpaRepository<CheckHistory, Long> {
}
