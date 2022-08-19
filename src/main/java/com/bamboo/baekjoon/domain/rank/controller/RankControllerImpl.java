package com.bamboo.baekjoon.domain.rank.controller;

import com.bamboo.baekjoon.domain.rank.dto.RankResponseDto;
import com.bamboo.baekjoon.domain.rank.service.RankService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

    @PutMapping("/admin/rank")
    @ApiOperation(value = "특정 Season의 랭킹을 업데이트한다.", notes = "seasonId에 해당하는 Season의 랭킹 정보를 미반영된 Check로부터 업데이트한다.")
    @ApiImplicitParam(name = "seasonId", value = "랭킹을 업데이트할 season의 id", required = true, example = "1")
    public String updateAccumRank(@RequestParam("seasonId") Long seasonId) {
        rankService.updateAccumRank(seasonId);

        return "updated";
    }

    // TODO: INACTIVE User는 제외
    @GetMapping("/rank/score")
    @ApiOperation(value = "랭킹 정보 조회",
            notes = "Pagination을 통해 랭킹 정보를 조회한다.\n조회 기준(sort)은 score_challenge, score_fail, score_total이다.")
    @ApiImplicitParam(name = "seasonId", value = "랭킹을 조회할 season의 id", required = true, example = "1")
    public ResponseEntity<Page<RankResponseDto.Score>> getAccumRankByScore(@RequestParam("seasonId") Long seasonId, Pageable pageable) {
        Page<RankResponseDto.Score> response = rankService.getAccumRankByScore(seasonId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
