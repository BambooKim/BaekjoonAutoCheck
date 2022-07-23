package com.bamboo.baekjoon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CheckHistory {

    @Id @GeneratedValue
    @Column(name = "history_id")
    private Long id;

    @Column(name = "prob_no", nullable = false)
    private int probNo;

    @Column(name = "prob_tier", nullable = false)
    private int probTier;

    @Column(name = "solved_at", nullable = false)
    private LocalDateTime solvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
