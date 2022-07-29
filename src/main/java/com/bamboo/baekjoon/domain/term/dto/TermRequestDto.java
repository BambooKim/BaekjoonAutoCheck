package com.bamboo.baekjoon.domain.term.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermRequestDto {

    @NotNull(message = "Season의 id를 입력해주세요.")
    @Positive(message = "id는 양수입니다.")
    private Long seasonId;

    @NotNull(message = "시작 날짜와 시간을 입력해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "마감 날짜와 시간을 입력해주세요.")
    private LocalDateTime endAt;
}
