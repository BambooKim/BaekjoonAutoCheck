package com.bamboo.baekjoon.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;


public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Creation {

        @NotNull(message = "이름을 입력해주세요.")
        private String korName;

        @NotNull(message = "입학년도를 입력해주세요.")
        @DecimalMin(value = "2015", message = "유효한 입학년도를 입력해주세요.")
        @DecimalMax(value = "2030", message = "유효한 입학년도를 입력해주세요.")
        private int enterYear;

        @NotNull(message = "백준 아이디를 입력해주세요.")
        private String bojId;

    }
}
