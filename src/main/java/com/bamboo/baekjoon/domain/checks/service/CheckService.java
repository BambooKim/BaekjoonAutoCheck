package com.bamboo.baekjoon.domain.checks.service;

import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CheckService {
    CheckResponseDto.Simple createCheck(CheckRequestDto.Create requestDto);

    CheckResponseDto.Simple findCheckSimpleById(Long id);

    Page<CheckResponseDto.Simple> getCheckSimpleAll(Pageable pageable);

    CheckResponseDto.Detail findCheckDetailById(Long id);

    Page<CheckResponseDto.Detail> getCheckDetailAll(Pageable pageable);

    Page<CheckResponseDto.Detail> getChecksByUserAndTerm(List<Long> userIdList, List<Long> termIdList, Pageable pageable);

    CheckResponseDto.Detail updateCheck(Long id, CheckRequestDto.Update requestDto);

    List<CheckResponseDto.Simple> createChecks(List<CheckRequestDto.Create> requestList);

    String deleteById(Long id);

    String deleteByParams(List<Long> params);

    List<CheckResponseDto.AfterRun> runCheck(List<Long> checkIdList);

    List<CheckResponseDto.AfterRun> runCheckByTerm(List<Long> termIdList);

    List<CheckResponseDto.AfterRun> runCheckByUser(List<Long> userIdList);

    List<CheckResponseDto.Simple> createCheckBySingleTerm(Long termId, List<Long> userIdList);

    Page<CheckResponseDto.UserSeason> getTermByUserAndSeason(Long userId, Long seasonId, Pageable pageable);

    Long countFailureCheck(Long userId, Long seasonId);

    Page<CheckResponseDto.History> getCheckHistory(List<Long> checkId, Pageable pageable);

    Page<CheckResponseDto.History> getUserCheckHistory(Long userId, Long checkId, Pageable pageable);
}
