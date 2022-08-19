package com.bamboo.baekjoon.domain.checks.controller;

import com.bamboo.baekjoon.domain.checks.dto.CheckRequestDto;
import com.bamboo.baekjoon.domain.checks.dto.CheckResponseDto;
import com.bamboo.baekjoon.domain.checks.service.CheckService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @PostMapping("/admin/check/runByUser")
    @ApiOperation(value = "userId에 의한 Check 실행", notes = "userId에 해당하는 Check를 실행한다. userId를 List로 보낸다. PENDING인 Check만")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIdList", value = "Check를 실행할 User의 Id List를 담은 JSON Body", required = true)
    })
    public ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckByUser(@RequestBody List<Long> userIdList) {
        List<CheckResponseDto.AfterRun> response = checkService.runCheckByUser(userIdList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/check/runByTerm")
    @ApiOperation(value = "termId에 의한 Check 실행", notes = "termId에 해당하는 Check를 실행한다. termId를 List로 보낸다. PENDING인 Check만")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "termIdList", value = "Check를 실행할 Term의 Id List를 담은 JSON Body", required = true)
    })
    public ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckByTerm(@RequestBody List<Long> termIdList) {
        List<CheckResponseDto.AfterRun> response = checkService.runCheckByTerm(termIdList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/check/runByCheck")
    @ApiOperation(value = "checkId에 의한 Check 실행", notes = "checkId에 해당하는 Check를 실행한다. checkId를 List로 보낸다. PENDING인 Check만")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "checkIdList", value = "Check를 실행할 Check의 Id List를 담은 JSON Body", required = true)
    })
    public ResponseEntity<List<CheckResponseDto.AfterRun>> runCheckById(@RequestBody List<Long> checkIdList) {
        List<CheckResponseDto.AfterRun> response = checkService.runCheck(checkIdList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/check")
    @ApiOperation(value = "단일 Check 생성", notes = "Check를 한 개 생성한다. (userId, termId)는 Unique이다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestDto", value = "termId와 userId를 담은 JSON Body", required = true, dataTypeClass = CheckRequestDto.Create.class)
    })
    public ResponseEntity<CheckResponseDto.Simple> createCheck(@RequestBody CheckRequestDto.Create requestDto) {
        CheckResponseDto.Simple response = checkService.createCheck(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/check-multiple")
    @ApiOperation(value = "복수의 Check 생성", notes = "Check를 여러 개 한꺼번에 생성한다. (userId, termId)는 Unique이다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestList", value = "termId와 userId의 List를 담은 JSON Body", required = true)
    })
    public ResponseEntity<List<CheckResponseDto.Simple>> createChecks(@RequestBody List<CheckRequestDto.Create> requestList) {
        List<CheckResponseDto.Simple> response = checkService.createChecks(requestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/check/term")
    @ApiOperation(value = "복수의 Check 생성", notes = "단일 termId-복수 userId로 Check를 여러 개 한꺼번에 생성한다. (userId, termId)는 Unique이다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "생성하려는 Check의 termId", required = true, dataTypeClass = Long.class, example = "4"),
            @ApiImplicitParam(name = "userIdList", value = "생성하려는 Check의 User Id List", required = true)
    })
    public ResponseEntity<List<CheckResponseDto.Simple>> createCheckBySingleTerm(@RequestParam(value = "id") Long termId,
                                                                                 @RequestBody List<Long> userIdList) {
        List<CheckResponseDto.Simple> response = checkService.createCheckBySingleTerm(termId, userIdList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check/{id}")
    @ApiOperation(value = "단일 Check 조회 (Simple)", notes = "checkId를 통해 Check를 한 개 조회한다. (Simple)")
    public ResponseEntity<CheckResponseDto.Simple> searchCheckSimpleById(
            @ApiParam(name = "id", value = "조회할 Check의 id", example = "1") @PathVariable("id") Long id) {
        CheckResponseDto.Simple response = checkService.findCheckSimpleById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check")
    @ApiOperation(value = "Check 조회 with Pagination (Simple)", notes = "Paginationa을 통해 전체 Check를 조회한다. (Simple)")
    public ResponseEntity<Page<CheckResponseDto.Simple>> getCheckSimpleAll(Pageable pageable) {
        Page<CheckResponseDto.Simple> response = checkService.getCheckSimpleAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-detail/{id}")
    @ApiOperation(value = "단일 Check 조회 (Detail)", notes = "checkId를 통해 Check를 한 개 조회한다. (Detail)")
    public ResponseEntity<CheckResponseDto.Detail> searchCheckDetailById(
            @ApiParam(name = "id", value = "조회할 Check의 id", example = "1") @PathVariable("id") Long id) {
        CheckResponseDto.Detail response = checkService.findCheckDetailById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-detail")
    @ApiOperation(value = "Check 조회 with Pagination (Detail)", notes = "Paginationa을 통해 전체 Check를 조회한다. (Detail)")
    public ResponseEntity<Page<CheckResponseDto.Detail>> getCheckDetailAll(Pageable pageable) {
        Page<CheckResponseDto.Detail> response = checkService.getCheckDetailAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/checks")
    @ApiOperation(value = "userId와 termId를 통해 Check 조회 with Pagination (Detail)",
            notes = "userId와 termId를 통해 Pagination된 Check Detail을 조회한다.\n" +
                    "이때 termId가 없다면 userId 기준으로만, userId가 없다면 termId 기준으로만 조회한다.\n" +
                    "둘 다 없다면 /check-detail와 동일하게 동작한다.")
    public ResponseEntity<Page<CheckResponseDto.Detail>> getCheckByUserAndTerm(
                                        @RequestParam(value = "userId", required = false) List<Long> userIdList,
                                        @RequestParam(value = "termId", required = false) List<Long> termIdList,
                                        Pageable pageable) {
        Page<CheckResponseDto.Detail> response = checkService.getChecksByUserAndTerm(userIdList, termIdList, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // TODO: 수정 시 랭킹 정보도 수정하는 로직 추가해야함
    @PutMapping("/admin/check/{id}")
    @ApiOperation(value = "Check 정보 수정", notes = "checkId가 id인 Check 정보를 수정한다.")
    public ResponseEntity<CheckResponseDto.Detail> updateCheck(
            @ApiParam(name = "id", value = "수정할 Check의 id", example = "1") @PathVariable("id") Long id,
                                                                   @RequestBody CheckRequestDto.Update requestDto) {
        CheckResponseDto.Detail response = checkService.updateCheck(id, requestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/admin/check/{id}")
    @ApiOperation(value = "Check 삭제", notes = "checkId가 id인 Check를 삭제한다.")
    public ResponseEntity<String> deleteCheck(
            @ApiParam(name = "id", value = "삭제할 Check의 id", example = "1") @PathVariable("id") Long id) {
        String response = checkService.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/admin/check")
    @ApiOperation(value = "Check 삭제 (복수)", notes = "checkId가 리스트에 있는 Check들을 삭제한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "삭제할 Check의 Id List", required = true)
    })
    public ResponseEntity<String> deleteChecks(@RequestParam(value = "id") List<Long> params) {
        String response = checkService.deleteByParams(params);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
