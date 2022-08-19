package com.bamboo.baekjoon.domain.checks.controller;

import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.List;


public interface CheckController {
    ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckByUser(List<Long> userIdList);
    ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckById(List<Long> checkIdList);
    ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckByTerm(List<Long> termIdList);
    ResponseEntity<CheckResponseDto.Simple> createCheck(CheckRequestDto.Create requestDto);
    ResponseEntity<List<CheckResponseDto.Simple>> createChecks(List<CheckRequestDto.Create> requestList);
    ResponseEntity<List<CheckResponseDto.Simple>> createCheckBySingleTerm(Long termId, List<Long> userIdList);
    ResponseEntity<CheckResponseDto.Simple> searchCheckSimpleById(Long id);
    ResponseEntity<Page<CheckResponseDto.Simple>> getCheckSimpleAll(Pageable pageable);
    ResponseEntity<CheckResponseDto.Detail> searchCheckDetailById(Long id);
    ResponseEntity<Page<CheckResponseDto.Detail>> getCheckDetailAll(Pageable pageable);
    ResponseEntity<Page<CheckResponseDto.Detail>> getCheckByUserAndTerm(
                                                    List<Long> userIdList, List<Long> termIdList, Pageable pageable);
    ResponseEntity<CheckResponseDto.Detail> updateCheck(Long id, CheckRequestDto.Update requestDto);
    ResponseEntity<String> deleteCheck(Long id);
    ResponseEntity<String> deleteChecks(List<Long> params);
}
