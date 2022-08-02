package com.bamboo.baekjoon.domain.rank;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "accum_rank")
public class AccumRank extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accum_rank_id")
    private Long id;

    @Column(name = "score_total", nullable = false)
    private int scoreTotal;

    @Column(name = "score_challenge", nullable = false)
    private int scoreChallenge;

    @Column(name = "score_fail", nullable = false)
    private int scoreFail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    public void addScoreFail(int score) {
        this.scoreFail += score;
    }

    public void addScoreTotal(int score) {
        this.scoreTotal += score;
    }

    public void addScoreChallenge(int score) {
        this.scoreChallenge += score;
    }

    @Builder
    public AccumRank(int scoreTotal, int scoreChallenge, int scoreFail, User user, Season season) {
        this.scoreTotal = scoreTotal;
        this.scoreChallenge = scoreChallenge;
        this.scoreFail = scoreFail;
        this.user = user;
        this.season = season;
    }

}
