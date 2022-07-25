package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.Checks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckRepository extends JpaRepository<Checks, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    List<Checks> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    Page<Checks> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    Optional<Checks> findById(Long aLong);
}
