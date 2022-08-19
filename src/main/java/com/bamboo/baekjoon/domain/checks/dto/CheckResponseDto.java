package com.bamboo.baekjoon.domain.checks.dto;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.FailureReason;
import com.bamboo.baekjoon.domain.term.dto.TermResponseDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public class CheckResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Simple {

        private Long id;
        private CheckStatus status;
        private boolean success;
        private UserResponseDto.Info userInfo;
        private TermResponseDto.Info termInfo;

        public static Simple of(Checks check) {
            return Simple.builder()
                    .id(check.getId())
                    .status(check.getStatus())
                    .success(check.isSuccess())
                    .userInfo(UserResponseDto.Info.of(check.getUser()))
                    .termInfo(TermResponseDto.Info.of(check.getTerm()))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Detail {

        private Long id;
        private CheckStatus status;
        private boolean success;
        private FailureReason reason;
        private LocalDateTime runAt;
        private UserResponseDto.Info userInfo;
        private TermResponseDto.Info termInfo;

        public static Detail of(Checks check) {
            return Detail.builder()
                    .id(check.getId())
                    .status(check.getStatus())
                    .success(check.isSuccess())
                    .reason(check.getReason())
                    .runAt(check.getRunAt())
                    .userInfo(UserResponseDto.Info.of(check.getUser()))
                    .termInfo(TermResponseDto.Info.of(check.getTerm()))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class AfterRun {

        @ApiModelProperty(value = "Check Id")
        private Long id;

        @ApiModelProperty(value = "Check Status")
        private CheckStatus status;

        @ApiModelProperty(value = "Check Result. True이면 성공, False이면 실패")
        private boolean success;

        @ApiModelProperty(value = "실패시 실패 사유. 성공이면 null")
        private FailureReason reason;

        @ApiModelProperty(value = "Check 실행 시간")
        private LocalDateTime runAt;

        public static AfterRun of(Checks check) {
            return AfterRun.builder()
                    .id(check.getId())
                    .status(check.getStatus())
                    .success(check.isSuccess())
                    .reason(check.getReason())
                    .runAt(check.getRunAt())
                    .build();
        }
    }
}
