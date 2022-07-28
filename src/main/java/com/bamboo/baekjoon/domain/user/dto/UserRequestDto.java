package com.bamboo.baekjoon.domain.user.dto;

import com.bamboo.baekjoon.domain.user.UserStatus;
import lombok.*;

import javax.validation.constraints.*;


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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Status {

        @NotEmpty(message = "User의 id를 입력해주세요.")
        @Positive(message = "id는 양수입니다.")
        private Long userId;

        @NotEmpty(message = "Status를 입력해주세요.")
        private UserStatus status;
    }
}
