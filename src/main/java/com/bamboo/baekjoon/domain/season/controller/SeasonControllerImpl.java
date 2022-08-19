package com.bamboo.baekjoon.domain.season.controller;

import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import com.bamboo.baekjoon.domain.season.service.SeasonService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SeasonControllerImpl implements SeasonController {

    private final SeasonService seasonService;

    @PostMapping("/admin/season")
    @ApiOperation(value = "Season 생성", notes = "Season을 한 개 생성한다.")
    @ApiImplicitParam(name = "requestDto", value = "생성할 Season의 정보를 담은 JSON Body", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "실행 성공"),
            @ApiResponse(responseCode = "400", description = "시작 날짜와 마감 날짜가 유효하지 않음"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SeasonResponseDto> createSeason(@RequestBody SeasonRequestDto requestDto) {
        SeasonResponseDto response = seasonService.createSeason(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/season/addUser")
    @ApiOperation(value = "Season-User 추가", notes = "존재하는 Season에 User들을 추가한다. 추가된 정보는 Rank에 반영된다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seasonId", value = "User를 추가할 Season의 Id", required = true, example = "1"),
            @ApiImplicitParam(name = "userIdList", value = "추가할 User의 Id List를 담은 JSON Body", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "실행 성공"),
            @ApiResponse(responseCode = "400", description = "이미 추가된 사용자가 존재함"),
            @ApiResponse(responseCode = "404", description = "Season을 찾을 수 없음"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> addSeasonUser(@RequestParam(value = "seasonId") Long seasonId,
                                           @RequestBody List<Long> userIdList) {
        Long addCount = seasonService.addSeasonUser(seasonId, userIdList);

        return ResponseEntity.status(HttpStatus.CREATED).body(addCount);
    }
}
