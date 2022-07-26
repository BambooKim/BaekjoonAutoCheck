package com.bamboo.baekjoon.domain.checks.controller;

import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.List;


public interface CheckController {

    ResponseEntity<CheckResponseDto.Simple> createCheck(CheckRequestDto.Create requestDto);
    ResponseEntity<List<CheckResponseDto.Simple>> createChecks(CheckRequestDto.CreateList requestList);
    ResponseEntity<CheckResponseDto.Simple> searchCheckSimpleById(Long id);
    ResponseEntity<Page<CheckResponseDto.Simple>> getCheckSimpleAll(Pageable pageable);
    ResponseEntity<CheckResponseDto.Detail> searchCheckDetailById(Long id);
    ResponseEntity<Page<CheckResponseDto.Detail>> getCheckDetailAll(Pageable pageable);
    ResponseEntity<CheckResponseDto.Detail> updateCheck(Long id, CheckRequestDto.Update requestDto);
    ResponseEntity<String> deleteCheck(Long id);
    ResponseEntity<String> deleteChecks(List<Long> params);
}
