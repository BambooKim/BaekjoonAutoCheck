package com.bamboo.baekjoon.domain.rank.service;

import com.bamboo.baekjoon.domain.rank.dto.RankResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RankService {

    void updateAccumRank(Long seasonId);

    Page<RankResponseDto.Score> getAccumRankByScore(Long seasonId, Pageable pageable);
}
