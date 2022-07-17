package com.bamboo.baekjoon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Rank {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "score_level", nullable = false)
    private int scoreByLevel;

    @Column(name = "score_continue", nullable = false)
    private int scoreByContinuity;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
