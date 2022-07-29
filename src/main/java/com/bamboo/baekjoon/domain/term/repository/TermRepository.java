package com.bamboo.baekjoon.domain.term.repository;

import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.term.Term;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {

    @Query("select count(t) from Term t where t.id in :termIdSet")
    long countTermsBySet(@Param("termIdSet") HashSet<Long> termIdSet);

    @Query("select t from Term t where t.id in :termIdSet")
    List<Term> selectTermsBySet(@Param("termIdSet") HashSet<Long> termIdSet);

    @EntityGraph(attributePaths = {"checks"})
    List<Term> findBySeasonIs(Season season);
}
