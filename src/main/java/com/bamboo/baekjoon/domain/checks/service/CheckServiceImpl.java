package com.bamboo.baekjoon.domain.checks.service;

import com.bamboo.baekjoon.domain.checks.CheckHistory;
import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.FailureReason;
import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.repository.CheckRepository;
import com.bamboo.baekjoon.domain.checks.repository.ChkHistoryRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {

    private final UserRepository userRepository;

    private final TermRepository termRepository;
    private final CheckRepository checkRepository;
    private final ChkHistoryRepository chkHistoryRepository;

    @Override
    public List<CheckResponseDto.AfterRun> runCheckByTerm(List<Long> termIdList) {

        return runCheckActual(checkRepository.findByStatusIsActiveAndTermIn(termIdList));
    }

    @Override
    public List<CheckResponseDto.AfterRun> runCheckByUser(List<Long> userIdList) {
        List<Checks> findCheckList = checkRepository.findByUserIn(userIdList);

        return runCheckActual(findCheckList);
    }

    @Override
    public List<CheckResponseDto.AfterRun> runCheck(List<Long> checkIdList) {
        List<Checks> findCheckList = checkRepository.findAllByStatusIsAndIdIn(CheckStatus.PENDING, checkIdList);

        return runCheckActual(findCheckList);
    }

    public List<CheckResponseDto.AfterRun> runCheckActual(List<Checks> findCheckList) {
        Map<User, List<Checks>> usersChecksListMap = new HashMap<>();

        LocalDateTime oldestStartAt = LocalDateTime.MAX;

        // 땡겨온 check들 iter하면서...
        for (Checks check : findCheckList) {
            if (check.getTerm().getEndAt().isAfter(LocalDateTime.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아직 마감이 되지 않았습니다.");

            if (oldestStartAt.isAfter(check.getTerm().getStartAt()))
                oldestStartAt = check.getTerm().getStartAt();

            if (usersChecksListMap.get(check.getUser()) == null) {
                List<Checks> list = new ArrayList<>();
                list.add(check);
                usersChecksListMap.put(check.getUser(), list);
            }

            usersChecksListMap.get(check.getUser()).add(check);
        }

        LocalDateTime finalOldestStartAt = oldestStartAt;
        usersChecksListMap.keySet().forEach(user -> {
            List<Checks> checks = usersChecksListMap.get(user);

            String urlHead = "https://www.acmicpc.net/status?problem_id=&user_id=";
            String urlTail = "&language_id=-1&result_id=4";
            String url = urlHead + user.getBojId() + urlTail;

            boolean isFirstPage = true;
            boolean isFirstRow = true;
            boolean needMorePage = true;
            String submitNo = "";

            while (needMorePage) {
                Connection connect = Jsoup.connect(url);
                try {
                    Elements tableRows = connect.get().getElementsByTag("tbody").first()
                            .getElementsByTag("tr");

                    for (Element tableRow : tableRows) {
                        if (!isFirstPage && isFirstRow) {
                            isFirstRow = false;

                            continue;
                        }

                        submitNo = tableRow.id().substring(9);

                        String solvedDateTimeString = tableRow.getElementsByAttribute("data-timestamp")
                                .get(0).attr("title");
                        LocalDateTime solvedDateTime
                                = LocalDateTime.parse(solvedDateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        if (solvedDateTime.isBefore(finalOldestStartAt)) {
                            needMorePage = false;
                            break;
                        }

                        Integer probNo = Integer.parseInt(tableRow.getElementsByAttributeValueContaining("href", "/problem")
                                .get(0).text());
                        String probInfoUrl = "https://solved.ac/api/v3/search/problem?query=" + probNo;

                        String block = WebClient.create(probInfoUrl).get().accept(MediaType.APPLICATION_JSON)
                                .retrieve().bodyToMono(String.class).block();

                        if (block == null)
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "문제 정보를 찾을 수 없습니다.");

                        int probTier = JsonParser.parseString(block).getAsJsonObject().get("items").getAsJsonArray().get(0)
                                .getAsJsonObject().get("level").getAsInt();

                        for (Checks check : checks) {

                            // CheckHistory가 Check당 한번만 생성되게 됨...
//                            if (check.getStatus() == CheckStatus.COMPLETE)
//                                continue;

                            Term term = check.getTerm();
                            if (solvedDateTime.isAfter(term.getStartAt()) && solvedDateTime.isBefore(term.getEndAt())) {
                                // probNo, probTier, solvedAt, user, check -> CheckHistory
                                // CheckHistory 생성

                                // Check 성공/실패 판정
                                if (user.getEnterYear() == 2022) {
                                    CheckHistory history = CheckHistory.builder()
                                            .probNo(probNo).probTier(probTier).solvedAt(solvedDateTime)
                                            .build();
                                    history.setCheck(check);
                                    chkHistoryRepository.save(history);

                                    check.admitCheck();
                                } else {
                                    if (probTier >= (user.getUserTier() - 5)) {
                                        CheckHistory history = CheckHistory.builder()
                                                .probNo(probNo).probTier(probTier).solvedAt(solvedDateTime)
                                                .build();
                                        history.setCheck(check);
                                        chkHistoryRepository.save(history);

                                        check.admitCheck();
                                    } else {
                                        // 하나의 Check에 여러 문제를 풀었는데, 처음 제출은 성공인데 다음 제출이 TIER_UNMATCH인 경우
                                        // -> 성공 처리해야함, 다시말해 성공이 아닐 때에만 진입하여 fail 처리해야함.
                                        if (!check.isSuccess())
                                            check.failCheck(FailureReason.TIER_UNMATCH);
                                    }
                                }

                                break;
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                isFirstPage = false;
                isFirstRow = true;
                url = "https://www.acmicpc.net/status?user_id=rlaqjarn1008&result_id=4&top=" + submitNo;
            }
        });

        /*
         * Create Response DTO List
         */
        List<CheckResponseDto.AfterRun> response = new ArrayList<>();
        findCheckList.forEach(check -> {
            if (check.getStatus() == CheckStatus.PENDING) {
                check.failCheck(FailureReason.NO_SUCCESS);
            }

            CheckResponseDto.AfterRun item = CheckResponseDto.AfterRun.of(check);
            response.add(item);
        });

        return response;
    }

    @Override
    public CheckResponseDto.Simple createCheck(CheckRequestDto.Create requestDto) {
        User findUser = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> {
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
                .build();
        check.setTerm(findTerm);
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
        List<User> users = userRepository.selectUsersIn(userIdSet);
        if (users.size() != userIdSet.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 userId가 존재합니다.");
        }

        List<Term> terms = termRepository.selectTermsBySet(termIdSet);
        if (terms.size() != termIdSet.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 termId가 존재합니다.");
        }

        // 땡겨온 애들은 아래에서 활용하기 위해 Map으로 변환 (key:id로 접근하기 위해)
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user, (a, b) -> b));
        Map<Long, Term> termMap = terms.stream().collect(Collectors.toMap(Term::getId, term -> term, (a, b) -> b));

        // requestList iter하면서 Check 생성 후 repository save -> response entity 생성 후 List add
        List<CheckResponseDto.Simple> responseList = new ArrayList<>();
        requestList.getItems().forEach(request -> {
            Checks check = Checks.builder()
                    .status(CheckStatus.PENDING)
                    .success(false)
                    .user(userMap.get(request.getUserId()))
                    .build();
            check.setTerm(termMap.get(request.getTermId()));
            checkRepository.save(check);

            responseList.add(CheckResponseDto.Simple.of(check));
        });

        return responseList;
    }

    @Override
    public List<CheckResponseDto.Simple> createCheckBySingleTerm(Long termId, List<Long> userIdList) {
        Term findTerm = termRepository.findById(termId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Term을 찾을 수 없습니다.");
        });

        List<User> findUsers;
        if (userIdList.isEmpty())
            findUsers = userRepository.findAllByStatusIs(UserStatus.ACTIVE);
        else
            findUsers = userRepository.selectUsersIn(userIdList);

        List<CheckResponseDto.Simple> responseList = new ArrayList<>();
        findUsers.forEach(user -> {
            Checks check = Checks.builder()
                    .status(CheckStatus.PENDING)
                    .success(false)
                    .user(user)
                    .build();
            check.setTerm(findTerm);
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
    // 이게 맞냐????

    @Override
    public Page<CheckResponseDto.Detail> getChecksByUserAndTerm
            (List<Long> userIdList, List<Long> termIdList, Pageable pageable) {
        if (userIdList == null) {
            if (termIdList == null) {           // user X, term X - 그냥 전체 조회
                return checkRepository.findAll(pageable).map(CheckResponseDto.Detail::of);
            } else {                            // user X, term O -> term 별로 묶어야 함!
                List<Term> terms = termRepository.findAllById(termIdList);

                return checkRepository.findByTermIn(terms, pageable).map(CheckResponseDto.Detail::of);
            }
        } else {
            List<User> users = userRepository.findAllById(userIdList);

            if (termIdList == null) {           // user O, term X -> user 별로 묶어야 함!
                return checkRepository.findByUserIn(users, pageable).map(CheckResponseDto.Detail::of);
            } else {                            // user O, term O
                List<Term> terms = termRepository.findAllById(termIdList);

                return checkRepository.findByUserInAndTermIn(users, terms, pageable)
                        .map(CheckResponseDto.Detail::of);
            }
        }
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

        findCheck.changeCheck(requestDto.getStatus(), requestDto.getSuccess(), requestDto.getReason(), findTerm);

        return CheckResponseDto.Detail.of(findCheck);
    }

    @Override
    public String deleteById(Long id) {
        checkRepository.findById(id).ifPresentOrElse(checkRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Check가 존재하지 않습니다.");
        });

        return "delete success";
    }

    @Override
    public String deleteByParams(List<Long> params) {
        if (params.size() != checkRepository.countByList(params))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 Check Id 요청이 있습니다.");

        if (params.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check Id를 입력해주세요.");

        checkRepository.deleteChecksByIdIn(params);

        return "delete success";
    }
}
