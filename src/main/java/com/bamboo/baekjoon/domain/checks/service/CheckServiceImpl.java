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

import java.util.*;
import java.util.stream.Collectors;

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
    public List<CheckResponseDto.Simple> createChecks(CheckRequestDto.CreateList requestList) {
        // requestList iter하면서 userId와 termId에 대한 HashSet 생성
        HashSet<Long> userIdSet = new HashSet<>();
        HashSet<Long> termIdSet = new HashSet<>();

        requestList.getItems().forEach(item -> {
            userIdSet.add(item.getUserId());
            termIdSet.add(item.getTermId());
        });

        // repository에 리스트 전달하여 (in query) status:ACTIVE user와 term 땡겨옴
        // 가져온거랑 리스트 개수 비교 -> 안맞으면 request로 들어온 값에 대해 없으므로 exception throw
        List<Users> users = userRepository.selectUsersBySet(userIdSet);
        if (users.size() != userIdSet.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 userId가 존재합니다.");
        }

        List<Term> terms = termRepository.selectTermsBySet(termIdSet);
        if (terms.size() != termIdSet.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 termId가 존재합니다.");
        }

        // 땡겨온 애들은 아래에서 활용하기 위해 Map으로 변환 (key:id로 접근하기 위해)
        Map<Long, Users> userMap = users.stream().collect(Collectors.toMap(Users::getId, user -> user, (a, b) -> b));
        Map<Long, Term> termMap = terms.stream().collect(Collectors.toMap(Term::getId, term -> term, (a, b) -> b));

        // requestList iter하면서 Check 생성 후 repository save -> response entity 생성 후 List add
        List<CheckResponseDto.Simple> responseList = new ArrayList<>();
        requestList.getItems().forEach(request -> {
            Checks check = Checks.builder()
                    .status(CheckStatus.PENDING)
                    .success(false)
                    .user(userMap.get(request.getUserId()))
                    .term(termMap.get(request.getTermId()))
                    .build();
            checkRepository.save(check);

            responseList.add(CheckResponseDto.Simple.of(check));
        });

        return responseList;
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

    @Override
    public String deleteById(Long id) {
        checkRepository.findById(id).ifPresentOrElse(checkRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Check가 존재하지 않습니다.");
        });

        return "delete success";
    }
}
