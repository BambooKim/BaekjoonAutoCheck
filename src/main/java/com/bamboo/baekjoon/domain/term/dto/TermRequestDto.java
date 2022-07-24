package com.bamboo.baekjoon.domain.term.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermRequestDto {

    @NotNull(message = "시작 날짜와 시간을 입력해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "마감 날짜와 시간을 입력해주세요.")
    private LocalDateTime endAt;
}
