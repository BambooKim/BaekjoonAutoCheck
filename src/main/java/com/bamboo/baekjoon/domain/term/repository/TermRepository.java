package com.bamboo.baekjoon.domain.term.repository;

import com.bamboo.baekjoon.domain.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
