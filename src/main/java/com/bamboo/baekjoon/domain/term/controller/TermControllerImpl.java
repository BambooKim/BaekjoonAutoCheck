package com.bamboo.baekjoon.domain.term.controller;

import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import com.bamboo.baekjoon.domain.term.service.TermService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TermControllerImpl implements TermController {

    private final TermService termService;

    @PostMapping("/admin/term")
    @ApiOperation(value = "Term 생성", notes = "Term을 한 개 생성한다.")
    @ApiImplicitParam(name = "requestDto", value = "생성할 Term의 정보를 담은 JSON Body", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "실행 성공"),
            @ApiResponse(responseCode = "400", description = "시작 날짜와 마감 날짜가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "Season을 찾을 수 없음"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TermResponseDto> createTerm(@Valid @RequestBody TermRequestDto requestDto) {
        TermResponseDto response = termService.createTerm(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/term/{id}")
    @ApiOperation(value = "단일 Term 조회", notes = "termId를 통해 Term 한 개를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Term을 찾을 수 없음"),
    })
    public ResponseEntity<TermResponseDto> searchTermById(
            @ApiParam(name = "id", value = "조회할 Term의 id", required = true, example = "1") @PathVariable("id") Long id) {
        TermResponseDto response = termService.findTermById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/term")
    @ApiOperation(value = "Season의 전체 Term 조회", notes = "Pagination을 통해 Season의 전체 Term을 조회한다.")
    @ApiImplicitParam(name = "seasonId", value = "Term을 조회할 Season의 id", required = true, example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Season을 찾을 수 없음"),
    })
    public ResponseEntity<Page<TermResponseDto>> getTermAll(@RequestParam("seasonId") Long seasonId, Pageable pageable) {
        Page<TermResponseDto> response = termService.getTermAll(seasonId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/admin/term/{id}")
    @ApiOperation(value = "Term 정보 수정", notes = "특정 Term의 정보를 수정한다.")
    @ApiImplicitParam(name = "requestDto", value = "수정할 Term의 정보", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "실행 성공"),
            @ApiResponse(responseCode = "400", description = "시작 날짜와 마감 날짜가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "Term 또는 Season을 찾을 수 없음"),
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<TermResponseDto> updateTerm(
            @ApiParam(name = "id", value = "수정할 Term의 id", required = true, example = "1") @PathVariable("id") Long id,
                                                        @Valid @RequestBody TermRequestDto requestDto) {
        TermResponseDto response = termService.updateTerm(id, requestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/admin/term/{id}")
    @ApiOperation(value = "Term 삭제", notes = "특정 Term을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "실행 성공"),
            @ApiResponse(responseCode = "404", description = "Term을 찾을 수 없음"),
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteTerm(
            @ApiParam(name = "id", value = "삭제할 Term의 id", required = true, example = "1") @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(termService.deleteById(id));
    }
}
