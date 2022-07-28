package com.bamboo.baekjoon.domain.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Users {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kor_name", length = 32, nullable = false)
    private String korName;

    @Column(name = "user_tier", nullable = true)
    private int userTier;

    @Column(name = "enter_year", nullable = false)
    private int enterYear;

    @Column(name = "boj_id", length = 64, nullable = false, unique = true)
    private String bojId;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public void changeTier(int userTier) {
        this.userTier = userTier;
    }
}
