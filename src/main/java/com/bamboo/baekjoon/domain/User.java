package com.bamboo.baekjoon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "kor_name", length = 32, nullable = false)
    private String korName;

    @Column(name = "user_tier", nullable = false)
    private int userTier;

    @Column(name = "enter_year", nullable = false)
    private int enterYear;

    @Column(name = "boj_id", length = 64, nullable = false, unique = true)
    private String bojId;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
