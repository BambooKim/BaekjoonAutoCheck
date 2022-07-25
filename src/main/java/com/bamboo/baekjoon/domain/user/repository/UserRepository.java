package com.bamboo.baekjoon.domain.user.repository;

import com.bamboo.baekjoon.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByBojId(String bojId);

    @Query("select count(u) from Users u where u.id in :userIdSet and u.status = 'ACTIVE'")
    long countUsersBySet(@Param("userIdSet") HashSet<Long> userIdSet);

    @Query("select u from Users u where u.id in :userIdSet and u.status = 'ACTIVE'")
    List<Users> selectUsersBySet(@Param("userIdSet") HashSet<Long> userIdSet);
}
