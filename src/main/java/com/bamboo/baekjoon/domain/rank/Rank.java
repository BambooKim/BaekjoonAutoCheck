package com.bamboo.baekjoon.domain.rank;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Rank extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "rank_id")
    private Long id;

    @Column(name = "score_level", nullable = false)
    private int scoreByLevel;

    @Column(name = "score_continue", nullable = false)
    private int scoreByContinuity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", scoreByLevel=" + scoreByLevel +
                ", scoreByContinuity=" + scoreByContinuity +
                ", user=" + user.getKorName() +
                '}';
    }
}
