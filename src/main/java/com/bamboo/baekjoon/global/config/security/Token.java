package com.bamboo.baekjoon.global.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class Token {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Request {
        @NotNull(message = "username을 입력해주세요.")
        private String username;

        @NotNull(message = "password를 입력해주세요.")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Response {
        private String token;

        public static Response of(String jwt) {
            return Response.builder()
                    .token(jwt)
                    .build();
        }
    }
}
