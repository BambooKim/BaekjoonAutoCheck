package com.bamboo.baekjoon.domain.user.repository;

import com.bamboo.baekjoon.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
