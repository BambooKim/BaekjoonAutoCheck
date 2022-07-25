package com.bamboo.baekjoon.domain.checks.controller;

import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CheckControllerImpl implements CheckController {

    private final CheckService checkService;

    @PostMapping("/check")
    public ResponseEntity<CheckResponseDto.Simple> createCheck(@RequestBody CheckRequestDto.Create requestDto) {
        CheckResponseDto.Simple response = checkService.createCheck(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/check-multiple")
    public ResponseEntity<List<CheckResponseDto.Simple>> createChecks(@RequestBody CheckRequestDto.CreateList requestList) {
        List<CheckResponseDto.Simple> response = checkService.createChecks(requestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<CheckResponseDto.Simple> searchCheckSimpleById(@PathVariable("id") Long id) {
        CheckResponseDto.Simple response = checkService.findCheckSimpleById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Page<CheckResponseDto.Simple>> getCheckSimpleAll(Pageable pageable) {
        Page<CheckResponseDto.Simple> response = checkService.getCheckSimpleAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-detail/{id}")
    public ResponseEntity<CheckResponseDto.Detail> searchCheckDetailById(@PathVariable("id") Long id) {
        CheckResponseDto.Detail response = checkService.findCheckDetailById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-detail")
    public ResponseEntity<Page<CheckResponseDto.Detail>> getCheckDetailAll(Pageable pageable) {
        Page<CheckResponseDto.Detail> response = checkService.getCheckDetailAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/check/{id}")
    public ResponseEntity<CheckResponseDto.Detail> updateCheck(@PathVariable("id") Long id,
                                                                   @RequestBody CheckRequestDto.Update requestDto) {
        CheckResponseDto.Detail response = checkService.updateCheck(id, requestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
