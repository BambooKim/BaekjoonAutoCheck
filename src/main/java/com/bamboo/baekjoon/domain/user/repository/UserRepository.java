package com.bamboo.baekjoon.domain.user.repository;

import com.bamboo.baekjoon.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByBojId(String bojId);
}
