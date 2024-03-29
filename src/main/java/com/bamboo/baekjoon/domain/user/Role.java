package com.bamboo.baekjoon.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자"),
    BLOCKED("ROLE_BLOCKED", "이용정지");

    private final String key;
    private final String title;
}
