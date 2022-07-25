package com.bamboo.baekjoon.domain.checks.service;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.repository.CheckRepository;
import com.bamboo.baekjoon.domain.checks.repository.ChkHistoryRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.Users;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {

    // TODO: {userId, termId} 는 UNIQUE여야 함.



    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final CheckRepository checkRepository;
    private final ChkHistoryRepository chkHistoryRepository;

    @Override
    public CheckResponseDto.Simple createCheck(CheckRequestDto.Create requestDto) {
        Users findUser = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");
        });

        if (findUser.getStatus() == UserStatus.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비활성화된 유저입니다.");
        }

        Term findTerm = termRepository.findById(requestDto.getTermId()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Term을 찾을 수 없습니다.");
        });

        Checks check = Checks.builder()
                .status(CheckStatus.PENDING)
                .success(false)
                .user(findUser)
                .term(findTerm)
                .build();

        checkRepository.save(check);

        return CheckResponseDto.Simple.of(check);
    }

    @Override
    public CheckResponseDto.Simple findCheckSimpleById(Long id) {
        Checks findCheck = checkRepository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Check가 존재하지 않습니다.");
        });

        return CheckResponseDto.Simple.of(findCheck);
    }

    @Override
    public Page<CheckResponseDto.Simple> getCheckSimpleAll(Pageable pageable) {
        return checkRepository.findAll(pageable).map(CheckResponseDto.Simple::of);
    }

    @Override
    public CheckResponseDto.Detail findCheckDetailById(Long id) {
        Checks findCheck = checkRepository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Check가 존재하지 않습니다.");
        });

        return CheckResponseDto.Detail.of(findCheck);
    }

    @Override
    public Page<CheckResponseDto.Detail> getCheckDetailAll(Pageable pageable) {
        return checkRepository.findAll(pageable).map(CheckResponseDto.Detail::of);
    }

    // TODO: enum에 대한 검증
    @Override
    public CheckResponseDto.Detail updateCheck(Long id, CheckRequestDto.Update requestDto) {
        Term findTerm = null;

        if (requestDto.getTermId() != null) {
            findTerm = termRepository.findById(requestDto.getTermId()).orElseThrow(() -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Term이 존재하지 않습니다.");
            });
        }

        Checks findCheck = checkRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Check가 존재하지 않습니다.");
        });

        findCheck.changeCheck(requestDto.getStatus(), requestDto.getSuccess(),
                requestDto.getReason(), findTerm);

        return CheckResponseDto.Detail.of(findCheck);
    }
}
