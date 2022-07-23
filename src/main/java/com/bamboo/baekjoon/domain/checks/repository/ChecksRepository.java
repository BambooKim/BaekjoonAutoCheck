package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.checks.Checks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecksRepository extends JpaRepository<Checks, Long> {
}
