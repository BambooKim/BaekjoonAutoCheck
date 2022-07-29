package com.bamboo.baekjoon.domain.checks.repository;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChkHistoryRepository extends JpaRepository<CheckHistory, Long> {

    @Query("select distinct ch from CheckHistory ch" +
            " join fetch ch.check c" +
            " join fetch c.user u" +
            " where c.term in :terms")
    List<CheckHistory> findHistoriesWithChecksWithUsersByTermIn(@Param("terms") List<Term> terms);
}
