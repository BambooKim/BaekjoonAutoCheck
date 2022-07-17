package com.bamboo.baekjoon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CheckHistory {

    @Id @GeneratedValue
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
