package com.bamboo.baekjoon.global.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Token {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Request {
        private String username;
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
