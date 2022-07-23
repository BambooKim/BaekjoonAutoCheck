package com.bamboo.baekjoon.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Rank {

    @Id @GeneratedValue
    @Column(name = "rank_id")
    private Long id;

    @Column(name = "score_level", nullable = false)
    private int scoreByLevel;

    @Column(name = "score_continue", nullable = false)
    private int scoreByContinuity;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
