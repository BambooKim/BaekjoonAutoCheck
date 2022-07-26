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

    CheckResponseDto.Detail updateCheck(Long id, CheckRequestDto.Update requestDto);

    List<CheckResponseDto.Simple> createChecks(CheckRequestDto.CreateList requestList);

    String deleteById(Long id);

    String deleteByParams(List<Long> params);
}
