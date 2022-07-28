package com.bamboo.baekjoon.domain.user.repository;

import com.bamboo.baekjoon.domain.user.UserTierHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierHistoryRepository extends JpaRepository<UserTierHistory, Long> {

}
