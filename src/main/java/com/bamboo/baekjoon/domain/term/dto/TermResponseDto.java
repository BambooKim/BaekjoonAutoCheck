package com.bamboo.baekjoon.domain.term.dto;

import com.bamboo.baekjoon.domain.term.Term;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermResponseDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public static TermResponseDto of(Term term) {
        return TermResponseDto.builder()
                .id(term.getId())
                .startAt(term.getStartAt())
                .endAt(term.getEndAt())
                .build();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Info {

        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endAt;

        public static TermResponseDto.Info of(Term term) {
            return TermResponseDto.Info.builder()
                    .id(term.getId())
                    .startAt(term.getStartAt())
                    .endAt(term.getEndAt())
                    .build();
        }
    }
}
