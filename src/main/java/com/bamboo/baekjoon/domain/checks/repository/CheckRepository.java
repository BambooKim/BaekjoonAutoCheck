package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CheckRepository extends JpaRepository<Checks, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    List<Checks> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    Page<Checks> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "term"})
    Page<Checks> findByUserInAndTermIn(List<Users> users, List<Term> terms, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "term"})
    Page<Checks> findByUserIn(List<Users> users, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "term"})
    Page<Checks> findByTermIn(List<Term> terms, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "term"})
    Optional<Checks> findById(Long aLong);

    @Query("select count(c) from Checks c where c.id in :paramList")
    long countByList(@Param("paramList") List<Long> params);

    @Modifying
    @Transactional
    @Query("delete from Checks c where c.id in :paramList")
    void deleteChecksByIdIn(@Param("paramList") List<Long> params);
}
