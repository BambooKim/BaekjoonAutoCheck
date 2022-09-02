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
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kor_name", length = 32, nullable = false)
    private String korName;

    @Column(name = "user_tier", nullable = true)
    private int userTier;

    @Column(name = "enter_year", nullable = false)
    private int enterYear;

    @Column(name = "boj_id", length = 64, nullable = false)
    private String bojId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public void changeTier(int userTier) {
        this.userTier = userTier;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    public void changePassword(String newEncryptedPw) {
        this.password = newEncryptedPw;
    }
}
