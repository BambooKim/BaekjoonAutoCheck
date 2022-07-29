package com.bamboo.baekjoon.domain.rank.controller;

import com.bamboo.baekjoon.domain.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
