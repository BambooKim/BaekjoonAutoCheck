package com.bamboo.baekjoon.domain.user.dto;

import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Creation {

        private Long id;
        private String korName;
        private int userTier;
        private int enterYear;
        private String bojId;
        private String username;
        private UserStatus status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime joinedAt;

        public static Creation of(User user) {
            return Creation.builder()
                    .id(user.getId())
                    .korName(user.getKorName())
                    .userTier(user.getUserTier())
                    .enterYear(user.getEnterYear())
                    .bojId(user.getBojId())
                    .username(user.getUsername())
                    .status(user.getStatus())
                    .joinedAt(user.getJoinedAt())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Info {

        private Long id;
        private String korName;
        private int userTier;
        private int enterYear;
        private String bojId;
        private UserStatus status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime joinedAt;

        public static Info of(User user) {
            return Info.builder()
                    .id(user.getId())
                    .korName(user.getKorName())
                    .userTier(user.getUserTier())
                    .enterYear(user.getEnterYear())
                    .bojId(user.getBojId())
                    .status(user.getStatus())
                    .joinedAt(user.getJoinedAt())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Tier {

        private Long id;
        private String korName;
        private int userTier;
        private String bojId;

        public static Tier of(User user) {
            return Tier.builder()
                    .id(user.getId())
                    .korName(user.getKorName())
                    .userTier(user.getUserTier())
                    .bojId(user.getBojId())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Status {

        private Long id;
        private String korName;
        private UserStatus status;

        public static Status of(User user) {
            return Status.builder()
                    .id(user.getId())
                    .korName(user.getKorName())
                    .status(user.getStatus())
                    .build();
        }
    }
}
