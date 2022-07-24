package com.bamboo.baekjoon.domain.checks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class CheckRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {

        @NotEmpty(message = "User의 id를 입력해주세요.")
        @Positive(message = "id는 양수입니다.")
        private Long userId;

        @NotEmpty(message = "Term의 id를 입력해주세요.")
        @Positive(message = "id는 양수입니다.")
        private Long termId;
    }
}
