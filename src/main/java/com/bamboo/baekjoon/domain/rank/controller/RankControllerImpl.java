package com.bamboo.baekjoon.domain.rank.controller;

import com.bamboo.baekjoon.domain.rank.dto.RankResponseDto;
import com.bamboo.baekjoon.domain.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RankControllerImpl implements RankController {

    private final RankService rankService;

    @PutMapping("/rank")
    public String updateAccumRank(@RequestParam("seasonId") Long seasonId) {
        rankService.updateAccumRank(seasonId);

        return "updated";
    }

    @GetMapping("/rank/score")
    public ResponseEntity<Page<RankResponseDto.Score>> getAccumRankByScore(@RequestParam("seasonId") Long seasonId, Pageable pageable) {
        Page<RankResponseDto.Score> response = rankService.getAccumRankByScore(seasonId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
