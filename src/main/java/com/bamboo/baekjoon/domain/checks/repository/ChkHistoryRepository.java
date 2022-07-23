package com.bamboo.baekjoon.domain.checks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChkHistoryRepository extends JpaRepository<ChecksRepository, Long> {
}
