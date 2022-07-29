package com.bamboo.baekjoon.domain.term.controller;

import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import com.bamboo.baekjoon.domain.term.service.TermService;
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

    // TODO: Season 추가 반영

    @PostMapping("/term")
    public ResponseEntity<TermResponseDto> createTerm(@Valid @RequestBody TermRequestDto requestDto) {
        TermResponseDto response = termService.createTerm(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/term/{id}")
    public ResponseEntity<TermResponseDto> searchTermById(@PathVariable("id") Long id) {
        TermResponseDto response = termService.findTermById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/term")
    public ResponseEntity<Page<TermResponseDto>> getTermAll(Pageable pageable) {
        Page<TermResponseDto> response = termService.getTermAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/term/{id}")
    public ResponseEntity<TermResponseDto> updateTerm(@PathVariable("id") Long id,
                                                        @Valid @RequestBody TermRequestDto requestDto) {
        TermResponseDto response = termService.updateTerm(id, requestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/term/{id}")
    public ResponseEntity<String> deleteTerm(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(termService.deleteById(id));
    }
}
